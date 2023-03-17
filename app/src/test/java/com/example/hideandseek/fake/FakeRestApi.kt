package com.example.hideandseek.fake

import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.datasource.remote.ResponseData
import com.example.hideandseek.data.datasource.remote.RestApi
import retrofit2.Response

class FakeRestApi : RestApi {
    override suspend fun getTest(): Response<ResponseData.ResponseGetTest> {
        return FakeDataSource.fakeResponseGetTest
    }

    override suspend fun postPlayerStatus(
        name: String,
        status: Int
    ): Response<ResponseData.ResponsePost> {
        return FakeDataSource.fakeResponsePost
    }

    override suspend fun getPlayer(secret_words: String): Response<List<ResponseData.ResponseGetPlayer>> {
        return FakeDataSource.fakeResponseGetPlayer
    }

    override suspend fun postPlayer(request: PostData.PostPlayer): Response<ResponseData.ResponsePost> {
        return FakeDataSource.fakeResponsePost
    }

    override suspend fun getLocation(
        secret_words: String,
        relative_time: String
    ): Response<List<ResponseData.ResponseGetLocation>> {
        return FakeDataSource.fakeResponseGetLocation
    }

    override suspend fun postLocation(request: PostData.PostLocation): Response<ResponseData.ResponsePost> {
        return FakeDataSource.fakeResponsePost
    }

    override suspend fun postRoomIsStart(
        secret_words: String,
        is_start: Int
    ): Response<ResponseData.ResponsePost> {
        return FakeDataSource.fakeResponsePost
    }

    override suspend fun getRoom(secret_words: String): Response<List<ResponseData.ResponseGetRoom>> {
        return FakeDataSource.fakeResponseGetRoom
    }

    override suspend fun postRoom(request: PostData.PostRoom): Response<ResponseData.ResponsePost> {
        return FakeDataSource.fakeResponsePost
    }
}
