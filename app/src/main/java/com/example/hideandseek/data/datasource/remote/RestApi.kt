package com.example.hideandseek.data.datasource.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApi {
    @GET("ping")
    suspend fun getTest(): Response<ResponseData.ResponseGetTest>

    @POST("api/v1/players/{id}/status/{status}")
    suspend fun postStatus(@Path("id") id: Int, @Path("status") status: Int): Response<ResponseData.ResponsePost>

    @GET("api/v1/players")
    suspend fun getPlayer(@Query("secret_words") secret_words: String): Response<List<ResponseData.ResponseGetPlayer>>

    @POST("api/v1/players")
    suspend fun postPlayer(@Body request: PostData.PostPlayer): Response<ResponseData.ResponsePost>

    @GET("api/v1/locations")
    suspend fun getSpacetime(@Query("time") time: String): Response<List<ResponseData.ResponseGetSpacetime>>

    @POST("api/v1/locations")
    suspend fun postSpacetime(@Body request: PostData.PostSpacetime): Response<ResponseData.ResponsePost>

    @POST("api/v1/rooms/{secret_words}/is_start/{is_start}")
    suspend fun postRoomIsStart(@Path("secret_words") secret_words: String, @Path("is_start") is_start: Int): Response<ResponseData.ResponsePost>

    @GET("api/v1/rooms")
    suspend fun getRoom(@Query("secret_words") secret_words: String): Response<List<ResponseData.ResponseGetRoom>>

    @POST("api/v1/rooms")
    suspend fun postRoom(@Body request: PostData.PostRoom): Response<ResponseData.ResponsePost>
}
