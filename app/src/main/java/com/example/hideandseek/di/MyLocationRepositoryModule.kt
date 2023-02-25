package com.example.hideandseek.di

import com.example.hideandseek.data.repository.MyLocationRepository
import com.example.hideandseek.data.repository.MyLocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MyLocationRepositoryModule {
    @Binds
    abstract fun bindMyLocationRepository(
        impl: MyLocationRepositoryImpl
    ) : MyLocationRepository
}
