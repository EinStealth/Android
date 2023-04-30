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

    fun writeIsOverSkillTime(isOverSkillTime: Boolean)

    fun writeLimitTime(limitTime: String)

    fun writeTrapTime(trapTime: String)

    fun writeSkillTime(skillTime: String)

    fun readName(): String

    fun readIcon(): Int

    fun raedLocation(): List<Float>

    fun readRelativeTime(): String

    fun readSecretWords(): String

    fun readIsOverSkillTime(): Boolean

    fun readLimitTime(): String

    fun readTrapTime(): String

    fun readSkillTime(): String
}

class MyInfoRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : MyInfoRepository {
    override fun writeName(name: String) {
        with(sharedPreferences.edit()) {
            this?.putString("name", name)
            this?.apply()
        }
    }

    override fun writeIcon(icon: Int) {
        with(sharedPreferences.edit()) {
            this?.putInt("icon", icon)
            this?.apply()
        }
    }

    override fun writeLocation(location: Location) {
        with(sharedPreferences.edit()) {
            this?.putFloat("latitude", location.latitude.toFloat())
            this?.putFloat("longitude", location.longitude.toFloat())
            this?.putFloat("altitude", location.altitude.toFloat())
            this?.putFloat("speed", location.speed)
            this?.apply()
        }
    }

    override fun writeRelativeTime(relativeTime: String) {
        with(sharedPreferences.edit()) {
            this?.putString("relativeTime", relativeTime)
            this?.apply()
        }
    }

    override fun writeSecretWords(secretWords: String) {
        with(sharedPreferences.edit()) {
            this?.putString("secretWords", secretWords)
            this?.apply()
        }
    }

    override fun writeIsOverSkillTime(isOverSkillTime: Boolean) {
        with(sharedPreferences.edit()) {
            this?.putBoolean("isOverSkillTime", isOverSkillTime)
            this?.apply()
        }
    }

    override fun writeLimitTime(limitTime: String) {
        with(sharedPreferences.edit()) {
            this?.putString("limitTime", limitTime)
            this?.apply()
        }
    }

    override fun writeTrapTime(trapTime: String) {
        with(sharedPreferences.edit()) {
            this?.putString("trapTime", trapTime)
            this?.apply()
        }
    }

    override fun writeSkillTime(skillTime: String) {
        with(sharedPreferences.edit()) {
            this?.putString("skillTime", skillTime)
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

    override fun readIsOverSkillTime(): Boolean {
        return sharedPreferences.getBoolean("isOverSkillTime", true)
    }

    override fun readLimitTime(): String {
        return sharedPreferences.getString("limitTime", "").toString()
    }

    override fun readTrapTime(): String {
        return sharedPreferences.getString("trapTime", "").toString()
    }

    override fun readSkillTime(): String {
        return sharedPreferences.getString("skillTime", "").toString()
    }
}
