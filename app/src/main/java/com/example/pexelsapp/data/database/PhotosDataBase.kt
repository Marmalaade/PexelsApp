package com.example.pexelsapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PhotosDataEntity::class],
    version = 1
)
abstract class PhotosDataBase : RoomDatabase() {
    abstract fun photosDao(): PhotosDataDao
}