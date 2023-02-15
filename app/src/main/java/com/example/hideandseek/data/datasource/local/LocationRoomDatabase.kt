package com.example.hideandseek.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationData::class], version = 1, exportSchema = false)
abstract class LocationRoomDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
}
