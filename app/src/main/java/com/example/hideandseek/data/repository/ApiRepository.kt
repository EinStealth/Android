package com.example.hideandseek.data.repository

import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.datasource.remote.RestApi
import com.example.hideandseek.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

interface ApiRepository {
    suspend fun getTest(): Response<ResponseData.ResponseGetTest>

    suspend fun postPlayerStatus(name: String, status: Int): Response<ResponseData.ResponsePost>

    suspend fun getPlayer(secretWords: String): Response<List<ResponseData.ResponseGetPlayer>>

    suspend fun postPlayer(request: PostData.PostPlayer): Response<ResponseData.ResponsePost>

    suspend fun getLocation(secretWords: String, relativeTime: String): Response<List<ResponseData.ResponseGetLocation>>

    suspend fun postLocation(request: PostData.PostLocation): Response<ResponseData.ResponsePost>

    suspend fun postRoomIsStart(secretWords: String, isStart: Int): Response<ResponseData.ResponsePost>

    suspend fun getRoom(secretWords: String): Response<List<ResponseData.ResponseGetRoom>>

    suspend fun postRoom(request: PostData.PostRoom): Response<ResponseData.ResponsePost>
}

class ApiRepositoryImpl @Inject constructor(
    private val restApiService: RestApi,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : ApiRepository {
    override suspend fun getTest(): Response<ResponseData.ResponseGetTest> =
        withContext(ioDispatcher) {
            restApiService.getTest()
        }

    override suspend fun postPlayerStatus(name: String, status: Int): Response<ResponseData.ResponsePost> =
        withContext(ioDispatcher) {
            restApiService.postPlayerStatus(name, status)
        }

    override suspend fun getPlayer(secretWords: String): Response<List<ResponseData.ResponseGetPlayer>> =
        withContext(ioDispatcher) {
            restApiService.getPlayer(secretWords)
        }

    override suspend fun postPlayer(request: PostData.PostPlayer): Response<ResponseData.ResponsePost> =
        withContext(ioDispatcher) {
            restApiService.postPlayer(request)
        }

    override suspend fun getLocation(secretWords: String, relativeTime: String): Response<List<ResponseData.ResponseGetLocation>> =
        withContext(ioDispatcher) {
            restApiService.getLocation(secretWords, relativeTime)
        }

    override suspend fun postLocation(request: PostData.PostLocation): Response<ResponseData.ResponsePost> =
        withContext(ioDispatcher) {
            restApiService.postLocation(request)
        }

    override suspend fun postRoomIsStart(secretWords: String, isStart: Int): Response<ResponseData.ResponsePost> =
        withContext(ioDispatcher) {
            restApiService.postRoomIsStart(secretWords, isStart)
        }

    override suspend fun getRoom(secretWords: String): Response<List<ResponseData.ResponseGetRoom>> =
        withContext(ioDispatcher) {
            restApiService.getRoom(secretWords)
        }

    override suspend fun postRoom(request: PostData.PostRoom): Response<ResponseData.ResponsePost> =
        withContext(ioDispatcher) {
            restApiService.postRoom(request)
        }
}
