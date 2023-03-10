package com.example.hideandseek.di

import com.example.hideandseek.data.repository.MapRepository
import com.example.hideandseek.data.repository.MapRepositoryImpl
import com.example.hideandseek.data.repository.MyInfoRepository
import com.example.hideandseek.data.repository.MyInfoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MyInfoRepositoryModule {
    @Binds
    abstract fun bindMyInfoRepository(
        impl: MyInfoRepositoryImpl
    ): MyInfoRepository
}
