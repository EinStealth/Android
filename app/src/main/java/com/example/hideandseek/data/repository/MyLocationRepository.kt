package com.example.hideandseek.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.Manifest
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.di.IODispatcher
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import java.time.LocalTime
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

interface MyLocationRepository {
    suspend fun start()

    fun stop()
}

class MyLocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val myInfoRepository: MyInfoRepository,
    private val apiRepository: ApiRepository,
    private val locationRepository: LocationRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val coroutineScope: CoroutineScope,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
): MyLocationRepository {
    // 現在地を更新するためのコールバック
    private lateinit var locationCallback: LocationCallback

    override suspend fun start() {
        // permission check
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        var secretWords: String = ""
        var relativeTime: LocalTime = LocalTime.now()

        // 直近の位置情報を取得
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null
                if (location != null) {
                    Log.d("MyLocation", location.toString())
                    myInfoRepository.writeLocation(location)
                    // ずれとを計算
                    val gap = calculateGap(location)
                    // 相対時間を計算
                    relativeTime = calculateRelativeTime(relativeTime, gap)
                    myInfoRepository.writeRelativeTime(relativeTime.toString().substring(0, 8))
                    // 10秒おきにAPI通信をする
                    if (relativeTime.second % 10 == 0) {
                        coroutineScope.launch {
                            withContext(ioDispatcher) {
                                secretWords = myInfoRepository.readSecretWords()
                                deleteAllLocation()
                                postLocation(secretWords, relativeTime, location)
                                getLocation(secretWords, relativeTime)
                            }
                        }
                    }
                }
            }

        // 位置情報リクエストの設定
        val locationRequest = LocationRequest.Builder(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        // 現在の位置情報を取得する
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(context)

        // 位置情報更新コールバックを定義する
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    Log.d("MyLocation", location.toString())
                    myInfoRepository.writeLocation(location)
                    // ずれとを計算
                    val gap = calculateGap(location)
                    // 相対時間を計算
                    relativeTime = calculateRelativeTime(relativeTime, gap)
                    myInfoRepository.writeRelativeTime(relativeTime.toString().substring(0, 8))
                    // 10秒おきにAPI通信をする
                    if (relativeTime.second % 10 == 0) {
                        coroutineScope.launch {
                            withContext(ioDispatcher) {
                                secretWords = myInfoRepository.readSecretWords()
                                deleteAllLocation()
                                postLocation(secretWords, relativeTime, location)
                                getLocation(secretWords, relativeTime)
                            }
                        }
                    }
                }
            }
        }

        // 位置情報の更新
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper(),
        )
    }

    override fun stop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    // 特殊相対性理論によりずれを計算する
    private fun calculateGap(location: Location): Long {
        Log.d("GAP", "speed: ${location.speed}, calc: ${(1000000000 * (1 - sqrt(1 - (location.speed / 10).pow(2)))).roundToInt().toLong()}")
        return (1000000000 * (1 - sqrt(1 - (location.speed / 10).pow(2)))).roundToInt().toLong()
    }

    private fun calculateRelativeTime(relativeTime: LocalTime, gap: Long): LocalTime {
        return relativeTime.minusNanos(gap).plusSeconds(1)
    }

    private suspend fun insertLocationAll(relativeTime: LocalTime, response: List<ResponseData.ResponseGetLocation>) {
        for (i in response.indices) {
            val user =
                com.example.hideandseek.data.datasource.local.LocationData(0, relativeTime.toString().substring(0, 8), response[i].latitude, response[i].longitude, 0.0, response[i].status)
            locationRepository.insert(user)
        }
    }

    private suspend fun postLocation(secretWords: String, relativeTime: LocalTime, location: Location) {
        try {
            val request = PostData.PostLocation(secretWords, relativeTime.toString().substring(0, 8),
                location.latitude, location.longitude, 0)
            val response = apiRepository.postLocation(request)
            if (response.isSuccessful) {
                Log.d("POST_TEST", "${response}\n${response.body()}")
            } else {
                Log.d("POST_TEST", "$response")
            }
        } catch (e: java.lang.Exception) {
            Log.d("POST_TEST", "$e")
        }
    }

    private suspend fun getLocation(secretWords: String, relativeTime: LocalTime) {
        try {
            val response = apiRepository.getLocation(secretWords, relativeTime.toString().substring(0, 8))
            if (response.isSuccessful) {
                Log.d("GET_TEST", "${response}\n${response.body()}")
                response.body()?.let { insertLocationAll(relativeTime, it) }
            } else {
                Log.d("GET_TEST", "$response")
            }
        } catch (e: java.lang.Exception) {
            Log.d("GET_TEST", "$e")
        }
    }

    // Locationデータベースのデータを全消去
    private suspend fun deleteAllLocation() {
        locationRepository.deleteAll()
    }
}
