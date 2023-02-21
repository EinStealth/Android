package com.example.hideandseek.data.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(room: RoomData)

    @Update
    fun update(room: RoomData)

    @Delete
    suspend fun delete(room: RoomData)

    @Query("SELECT * FROM room_table1")
    fun getAll(): Flow<List<RoomData>>

    @Query("DELETE FROM room_table1")
    suspend fun deleteAll()
}
