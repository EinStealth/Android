package com.example.hideandseek.data.repository

import androidx.annotation.WorkerThread
import com.example.hideandseek.data.datasource.local.LocationDao
import com.example.hideandseek.data.datasource.local.LocationData
import com.example.hideandseek.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface LocationRepository {
    val allLocations: Flow<List<LocationData>>

    suspend fun insert(location: LocationData)

    suspend fun deleteAll()
}

class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LocationRepository {

    override val allLocations: Flow<List<LocationData>>
        get() = locationDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(user: LocationData) {
        withContext(ioDispatcher) {
            locationDao.insert(user)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun deleteAll() {
        withContext(ioDispatcher) {
            locationDao.deleteAll()
        }
    }
}
