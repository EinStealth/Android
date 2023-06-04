package com.example.hideandseek.data.datasource.remote

import com.squareup.moshi.Json

class PostData {
    data class PostLocation(
        @Json(name = "secret_words") val secret_words: String,
        @Json(name = "relative_time") val relative_time: String,
        @Json(name = "latitude") val latitude: Double,
        @Json(name = "longitude") val longitude: Double,
        @Json(name = "status") var status: Int,
    )

    data class PostRoom(
        @Json(name = "secret_words") val secret_words: String,
        @Json(name = "is_start") val is_start: Int,
        @Json(name = "deamon") val deamon: Int,
    )

    data class PostPlayer(
        @Json(name = "secret_words") val secret_words: String,
        @Json(name = "name") val name: String,
        @Json(name = "icon") val icon: Int,
        @Json(name = "status") val status: Int,
    )
}
