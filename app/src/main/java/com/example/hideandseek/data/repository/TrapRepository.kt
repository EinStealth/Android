package com.example.hideandseek.data.repository

import androidx.annotation.WorkerThread
import com.example.hideandseek.data.datasource.local.TrapDao
import com.example.hideandseek.data.datasource.local.TrapData
import com.example.hideandseek.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TrapRepository {
    val allTraps: Flow<List<TrapData>>

    suspend fun insert(trap: TrapData)

    suspend fun deleteAll()
}

class TrapRepositoryImpl @Inject constructor(
    private val trapDao: TrapDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : TrapRepository {

    override val allTraps: Flow<List<TrapData>>
        get() = trapDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(trap: TrapData) {
        withContext(ioDispatcher) {
            trapDao.insert(trap)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun deleteAll() {
        withContext(ioDispatcher) {
            trapDao.deleteAll()
        }
    }
}
