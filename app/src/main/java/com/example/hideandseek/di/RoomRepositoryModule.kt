package com.example.hideandseek.di

import com.example.hideandseek.data.repository.RoomRepository
import com.example.hideandseek.data.repository.RoomRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RoomRepositoryModule {
    @Binds
    abstract fun bindRoomRepository(
        impl: RoomRepositoryImpl
    ): RoomRepository
}
