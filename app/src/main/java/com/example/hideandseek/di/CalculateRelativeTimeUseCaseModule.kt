package com.example.hideandseek.di

import com.example.hideandseek.domain.CalculateRelativeTimeUseCase
import com.example.hideandseek.domain.CalculateRelativeTimeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CalculateRelativeTimeUseCaseModule {
    @Binds
    abstract fun bindCalculateRelativeTimeUseCase(
        impl: CalculateRelativeTimeUseCaseImpl
    ) : CalculateRelativeTimeUseCase
}
