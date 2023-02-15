package com.example.hideandseek.data.repository

import androidx.annotation.WorkerThread
import com.example.hideandseek.data.datasource.local.UserDao
import com.example.hideandseek.data.datasource.local.UserData
import com.example.hideandseek.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UserRepository {
    val allUsers: Flow<List<UserData>>

    suspend fun getLatest(): UserData

    suspend fun insert(user: UserData)

    suspend fun deleteAll()
}

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override val allUsers: Flow<List<UserData>>
        get() = userDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun getLatest(): UserData {
        return withContext(ioDispatcher) {
            return@withContext userDao.getLatest()
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun insert(user: UserData) {
        withContext(ioDispatcher) {
            userDao.insert(user)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    override suspend fun deleteAll() {
        withContext(ioDispatcher) {
            userDao.deleteAll()
        }
    }
}
