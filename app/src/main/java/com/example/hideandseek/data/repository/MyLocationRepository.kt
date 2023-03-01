package com.example.hideandseek.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.Manifest
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface MyLocationRepository {
    fun start()

    fun stop()
}

class MyLocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val myInfoRepository: MyInfoRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient
): MyLocationRepository {
    // 現在地を更新するためのコールバック
    private lateinit var locationCallback: LocationCallback

    override fun start() {
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

        // 直近の位置情報を取得
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null
                if (location != null) {
                    Log.d("MyLocation", location.toString())
                    myInfoRepository.writeLocation(location)
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
}
