package com.example.hideandseek.data.datasource.remote

import com.squareup.moshi.Json

class ResponseData {
    data class ResponseGetTest(
        @Json(name = "message") val message: String,
    )

    data class ResponsePost(
        @Json(name = "message") val message: String,
    )

    data class ResponseGetLocation(
        @Json(name = "secret_words") val secret_words: String,
        @Json(name = "relative_time") val relative_time: String,
        @Json(name = "latitude") val latitude: Double,
        @Json(name = "longitude") val longitude: Double,
        @Json(name = "status") var status: Int,
    )

    data class ResponseGetRoom(
        @Json(name = "secret_words") val secret_words: String,
        @Json(name = "is_start") val is_start: Int,
    )

    data class ResponseGetPlayer(
        @Json(name = "secret_words") val secret_words: String,
        @Json(name = "name") val name: String,
        @Json(name = "icon") val icon: Int,
        @Json(name = "status") val status: Int,
    )
}
