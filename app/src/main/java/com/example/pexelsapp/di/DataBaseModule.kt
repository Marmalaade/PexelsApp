package com.example.pexelsapp.di

import android.content.Context
import androidx.room.Room
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.data.database.PhotosDataDao
import com.example.pexelsapp.data.database.PhotosDataDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    fun providePhotosDataBase(@ApplicationContext context: Context): PhotosDataDataBase {
        return Room.databaseBuilder(context, PhotosDataDataBase::class.java, AppConfig.getDataBaseName())
            .build()
    }

    @Provides
    fun providePhotosDao(database: PhotosDataDataBase): PhotosDataDao = database.photosDao()

}