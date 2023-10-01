package com.example.pexelsapp.di

import com.example.pexelsapp.data.PhotosRepositoryImpl
import com.example.pexelsapp.domain.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainRepositoryModule {
    @Binds
    abstract fun getRepository(impl: PhotosRepositoryImpl): PhotosRepository
}