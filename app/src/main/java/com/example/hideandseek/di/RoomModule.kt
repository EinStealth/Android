package com.example.hideandseek.di

import android.content.Context
import androidx.room.Room
import com.example.hideandseek.data.datasource.local.LocationDao
import com.example.hideandseek.data.datasource.local.LocationRoomDatabase
import com.example.hideandseek.data.datasource.local.RoomDao
import com.example.hideandseek.data.datasource.local.RoomRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideRoomRoomDatabase(
        @ApplicationContext context: Context
    ): RoomRoomDatabase {
        return Room.databaseBuilder(
            context,
            RoomRoomDatabase::class.java,
            "room_db1"
        ).build()
    }

    @Singleton
    @Provides
    fun provideRoomDao(db: RoomRoomDatabase): RoomDao {
        return db.roomDao()
    }
}
