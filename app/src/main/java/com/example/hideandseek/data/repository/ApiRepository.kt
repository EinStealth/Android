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

    suspend fun postStatus(id: Int, status: Int): Response<ResponseData.ResponsePost>

    suspend fun getSpacetime(time: String): Response<List<ResponseData.ResponseGetSpacetime>>

    suspend fun postSpacetime(request: PostData.PostSpacetime): Response<ResponseData.ResponsePost>

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

    override suspend fun postStatus(id: Int, status: Int): Response<ResponseData.ResponsePost> =
        withContext(ioDispatcher) {
            restApiService.postStatus(id, status)
        }

    override suspend fun getSpacetime(time: String): Response<List<ResponseData.ResponseGetSpacetime>> =
        withContext(ioDispatcher) {
            restApiService.getSpacetime(time)
        }

    override suspend fun postSpacetime(request: PostData.PostSpacetime): Response<ResponseData.ResponsePost> =
        withContext(ioDispatcher) {
            restApiService.postSpacetime(request)
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
