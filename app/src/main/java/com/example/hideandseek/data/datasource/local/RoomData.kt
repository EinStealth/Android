package com.example.hideandseek.data.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_table1")
data class RoomData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "secret_words") val secretWords: String,
    @ColumnInfo(name = "is_start") val isStart: Boolean,
)
