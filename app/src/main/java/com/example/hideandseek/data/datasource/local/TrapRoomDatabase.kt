package com.example.hideandseek.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TrapData::class], version = 1, exportSchema = false)
abstract class TrapRoomDatabase : RoomDatabase() {

    abstract fun trapDao(): TrapDao
}
