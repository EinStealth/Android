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

    @POST("api/v1/players/{name}/status/{status}")
    suspend fun postPlayerStatus(@Path("name") name: String, @Path("status") status: Int): Response<ResponseData.ResponsePost>

    @GET("api/v1/players")
    suspend fun getPlayer(@Query("secret_words") secret_words: String): Response<List<ResponseData.ResponseGetPlayer>>

    @POST("api/v1/players")
    suspend fun postPlayer(@Body request: PostData.PostPlayer): Response<ResponseData.ResponsePost>

    @GET("api/v1/locations")
    suspend fun getLocation(@Query("secret_words") secret_words: String, @Query("relative_time") relative_time: String): Response<List<ResponseData.ResponseGetLocation>>

    @POST("api/v1/locations")
    suspend fun postLocation(@Body request: PostData.PostLocation): Response<ResponseData.ResponsePost>

    @POST("api/v1/rooms/{secret_words}/is_start/{is_start}")
    suspend fun postRoomIsStart(@Path("secret_words") secret_words: String, @Path("is_start") is_start: Int): Response<ResponseData.ResponsePost>

    @POST("api/v1/rooms/{secret_words}/deamon/{deamon}")
    suspend fun postRoomDemon(@Path("secret_words") secret_words: String, @Path("deamon") deamon: Int): Response<ResponseData.ResponsePost>

    @GET("api/v1/rooms")
    suspend fun getRoom(@Query("secret_words") secret_words: String): Response<List<ResponseData.ResponseGetRoom>>

    @POST("api/v1/rooms")
    suspend fun postRoom(@Body request: PostData.PostRoom): Response<ResponseData.ResponsePost>
}
