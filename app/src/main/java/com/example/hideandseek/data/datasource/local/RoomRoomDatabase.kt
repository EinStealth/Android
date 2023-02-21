package com.example.hideandseek.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomData::class], version = 1, exportSchema = false)
abstract class RoomRoomDatabase : RoomDatabase() {

    abstract fun roomDao(): RoomDao
}
