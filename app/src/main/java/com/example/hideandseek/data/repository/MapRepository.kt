package com.example.hideandseek.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.hideandseek.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import java.net.URL
import javax.inject.Inject

interface MapRepository {
    suspend fun fetchMap(url: String): Bitmap

    fun getOriginalBitmap(url: String): Bitmap
}

class MapRepositoryImpl @Inject constructor(
    private val coroutineScope: CoroutineScope,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : MapRepository {

    override suspend fun fetchMap(url: String): Bitmap {
        val originalDeferred = coroutineScope.async(ioDispatcher) {
            getOriginalBitmap(url)
        }
        return originalDeferred.await()
    }

    override fun getOriginalBitmap(url: String): Bitmap =
        URL(url).openStream().use {
            BitmapFactory.decodeStream(it)
        }
}
