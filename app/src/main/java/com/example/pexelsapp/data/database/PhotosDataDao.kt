package com.example.pexelsapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotosDataDao {
    @Query("SELECT * FROM photos_data_table")
    fun getAllPhotos(): List<PhotosDataEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photos: PhotosDataEntity)

    @Query("DELETE FROM photos_data_table WHERE photo_id = :photoId")
    fun deletePhoto(photoId: Int)

    @Query("SELECT * FROM photos_data_table WHERE photo_id = :photoId")
    fun getPhoto(photoId: Int): PhotosDataEntity?

}