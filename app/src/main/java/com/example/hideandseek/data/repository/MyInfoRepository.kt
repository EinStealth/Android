package com.example.hideandseek.data.repository

import android.content.SharedPreferences
import android.location.Location
import javax.inject.Inject

interface MyInfoRepository {
    fun start()

    fun writeName(name: String)

    fun writeIcon(icon: Int)

    fun readName(): String

    fun readIcon(): Int

    fun raedLocation()

    fun readRelativeTime(): String
}

class MyInfoRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): MyInfoRepository {
    override fun start() {
        // TODO("Not yet implemented")
    }

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

    override fun readName(): String {
        val name = sharedPreferences.getString("name", "")
        return name.toString()
    }

    override fun readIcon(): Int {
        return sharedPreferences.getInt("icon", 0)
    }

    override fun raedLocation() {
        // TODO("Not yet implemented")
        return
    }

    override fun readRelativeTime(): String {
        // TODO("Not yet implemented")
        return ""
    }

}