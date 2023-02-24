package com.example.hideandseek.domain

import android.location.Location
import android.util.Log
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.data.repository.MyLocationRepository
import com.example.hideandseek.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.time.LocalTime
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class CalculateRelativeTimeUseCase @Inject constructor(
    private val myLocationRepository: MyLocationRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
){
    suspend operator fun invoke(): Flow<UserData> =
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
}