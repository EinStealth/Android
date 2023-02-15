package com.example.hideandseek.di

import com.example.hideandseek.data.repository.TrapRepository
import com.example.hideandseek.data.repository.TrapRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TrapRepositoryModule {
    @Binds
    abstract fun bindTrapRepository(
        impl: TrapRepositoryImpl
    ): TrapRepository
}
