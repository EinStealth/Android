package com.example.hideandseek.domain

import android.location.Location
import android.util.Log
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.repository.ApiRepository
import com.example.hideandseek.data.repository.LocationRepository
import com.example.hideandseek.data.repository.MyLocationRepository
import com.example.hideandseek.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.time.LocalTime
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

interface CalculateRelativeTimeUseCase {
    suspend operator fun invoke(): Flow<UserData>
}

class CalculateRelativeTimeUseCaseImpl @Inject constructor(
    private val myLocationRepository: MyLocationRepository,
    private val apiRepository: ApiRepository,
    private val locationRepository: LocationRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): CalculateRelativeTimeUseCase {
    override suspend operator fun invoke(): Flow<UserData> =
        withContext(ioDispatcher) {
            myLocationRepository.start()
            val result = flow {
                val location = myLocationRepository.flowLatestLocation
                var relativeTime: LocalTime = LocalTime.now()
                Log.d("UseCaseRelativeTimeInit", relativeTime.toString())
                location.collect {
                    Log.d("UseCaseRelativeTime", relativeTime.toString())
                    // ずれとを計算
                    val gap = calculateGap(it)
                    // 相対時間を計算
                    relativeTime = calculateRelativeTime(relativeTime, gap)
                    // 10秒おきにAPI通信をする
                    if (relativeTime.second % 10 == 0) {
                        deleteAllLocation()
                        postSpacetime(relativeTime, it)
                        getSpacetime(relativeTime)
                    }
                    emit(UserData(0, relativeTime.toString().substring(0, 8), it.latitude, it.longitude, it.altitude))
                }
            }
            result
        }

    // 特殊相対性理論によりずれを計算する
    private fun calculateGap(location: Location): Long {
        Log.d("GAP", "speed: ${location.speed}, calc: ${(1000000000 * (1 - sqrt(1 - (location.speed / 10).pow(2)))).roundToInt().toLong()}")
        return (1000000000 * (1 - sqrt(1 - (location.speed / 10).pow(2)))).roundToInt().toLong()
    }

    private fun calculateRelativeTime(relativeTime: LocalTime, gap: Long): LocalTime {
        return relativeTime.minusNanos(gap).plusSeconds(1)
    }

    private suspend fun insertLocationAll(relativeTime: LocalTime, response: List<ResponseData.ResponseGetSpacetime>) {
        for (i in response.indices) {
            val user =
                com.example.hideandseek.data.datasource.local.LocationData(0, relativeTime.toString().substring(0, 8), response[i].Latitude, response[i].Longtitude, response[i].Altitude, response[i].ObjId)
            locationRepository.insert(user)
        }
    }

    private suspend fun postSpacetime(relativeTime: LocalTime, location: Location) {
        try {
            val request = PostData.PostSpacetime(relativeTime.toString().substring(0, 8), location.latitude, location.longitude, location.altitude, 0)
            val response = apiRepository.postSpacetime(request)
            if (response.isSuccessful) {
                Log.d("POST_TEST", "${response}\n${response.body()}")
            } else {
                Log.d("POST_TEST", "$response")
            }
        } catch (e: java.lang.Exception) {
            Log.d("POST_TEST", "$e")
        }
    }

    private suspend fun getSpacetime(relativeTime: LocalTime) {
        try {
            val response = apiRepository.getSpacetime(relativeTime.toString().substring(0, 8))
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
