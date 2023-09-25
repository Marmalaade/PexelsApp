package com.example.pexelsapp.di

import com.example.pexelsapp.data.MainRepositoryImpl
import com.example.pexelsapp.domain.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainRepositoryModule {
    @Binds
    abstract fun getRepository(impl: MainRepositoryImpl): MainRepository
}