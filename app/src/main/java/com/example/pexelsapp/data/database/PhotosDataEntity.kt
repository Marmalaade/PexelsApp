package com.example.pexelsapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos_data_table")
data class PhotosDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo("photo_id") val photosDataId: Int,

    @ColumnInfo("photographer") val photographer: String,

    @ColumnInfo("url") val url: String
)