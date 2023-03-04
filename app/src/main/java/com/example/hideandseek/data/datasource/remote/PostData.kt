package com.example.hideandseek.data.datasource.remote

import com.squareup.moshi.Json

class PostData {
    data class PostSpacetime(
        @Json(name = "time") val time: String,
        @Json(name = "latitude") val latitude: Double,
        @Json(name = "longitude") val longitude: Double,
        @Json(name = "altitude") val altitude: Double,
        @Json(name = "obj_id") var objId: Int,
    )

    data class PostRoom(
        @Json(name = "secret_words") val secret_words: String,
        @Json(name = "is_start") val is_start: Int,
    )
}
