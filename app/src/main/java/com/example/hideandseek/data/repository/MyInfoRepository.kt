package com.example.hideandseek.data.repository

import android.content.SharedPreferences
import android.location.Location
import javax.inject.Inject

interface MyInfoRepository {
    fun writeName(name: String)

    fun writeIcon(icon: Int)

    fun writeLocation(location: Location)

    fun writeRelativeTime(relativeTime: String)

    fun writeSecretWords(secretWords: String)

    fun readName(): String

    fun readIcon(): Int

    fun raedLocation(): List<Float>

    fun readRelativeTime(): String

    fun readSecretWords(): String
}

class MyInfoRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : MyInfoRepository {
    override fun writeName(name: String) {
        with (sharedPreferences.edit()) {
            this?.putString("name", name)
            this?.apply()
        }
    }

    override fun writeIcon(icon: Int) {
        with (sharedPreferences.edit()) {
            this?.putInt("icon", icon)
            this?.apply()
        }
    }

    override fun writeLocation(location: Location) {
        with (sharedPreferences.edit()) {
            this?.putFloat("latitude", location.latitude.toFloat())
            this?.putFloat("longitude", location.longitude.toFloat())
            this?.putFloat("altitude", location.altitude.toFloat())
            this?.putFloat("speed", location.speed)
            this?.apply()
        }
    }

    override fun writeRelativeTime(relativeTime: String) {
        with (sharedPreferences.edit()) {
            this?.putString("relativeTime", relativeTime)
            this?.apply()
        }
    }

    override fun writeSecretWords(secretWords: String) {
        with (sharedPreferences.edit()) {
            this?.putString("secretWords", secretWords)
            this?.apply()
        }
    }

    override fun readName(): String {
        return sharedPreferences.getString("name", "").toString()
    }

    override fun readIcon(): Int {
        return sharedPreferences.getInt("icon", 0)
    }

    override fun raedLocation(): List<Float> {
        val latitude = sharedPreferences.getFloat("latitude", 0f)
        val longitude = sharedPreferences.getFloat("longitude", 0f)
        val altitude = sharedPreferences.getFloat("altitude", 0f)
        val speed = sharedPreferences.getFloat("speed", 0f)
        return listOf(latitude, longitude, altitude, speed)
    }

    override fun readRelativeTime(): String {
        return sharedPreferences.getString("relativeTime", "").toString()
    }

    override fun readSecretWords(): String {
        return sharedPreferences.getString("secretWords", "").toString()
    }
}
