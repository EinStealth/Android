package com.example.hideandseek.data.repository

import androidx.annotation.WorkerThread
import com.example.hideandseek.data.datasource.local.LocationDao
import com.example.hideandseek.data.datasource.local.LocationData
import com.example.hideandseek.data.datasource.local.RoomDao
import com.example.hideandseek.data.datasource.local.RoomData
import com.example.hideandseek.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RoomRepository {
    val allRoom: Flow<List<RoomData>>

    suspend fun insert(room: RoomData)

    suspend fun delete(room: RoomData)

    suspend fun deleteAll()
}

class RoomRepositoryImpl @Inject constructor(
    private val roomDao: RoomDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : RoomRepository {

    override val allRoom: Flow<List<RoomData>>
        get() = roomDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(room: RoomData) {
        withContext(ioDispatcher) {
            roomDao.insert(room)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun delete(room: RoomData) {
        withContext(ioDispatcher) {
            roomDao.delete(room)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun deleteAll() {
        withContext(ioDispatcher) {
            roomDao.deleteAll()
        }
    }
}
