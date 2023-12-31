package com.example.pexelsapp.data.mappers

import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.data.apimodels.CuratedPhotoResponse
import com.example.pexelsapp.data.apimodels.RequestResponse
import com.example.pexelsapp.data.database.PhotosDataEntity
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import javax.inject.Inject

class DataMapper @Inject constructor() {
    fun mapToRequestModel(unmapped: RequestResponse): RequestModel =
        RequestModel(
            title = unmapped.title ?: AppConfig.getEmptyString()
        )

    fun mapToCuratedPhotoModel(unmapped: CuratedPhotoResponse): CuratedPhotoModel = with(unmapped) {
        CuratedPhotoModel(
            id = id ?: AppConfig.getEmptyInt(),
            url = src?.url ?: AppConfig.getEmptyString(),
            photographer = photographer ?: AppConfig.getEmptyString()
        )
    }

    fun mapFromPhotosDataEntityToModel(entity: PhotosDataEntity): CuratedPhotoModel = with(entity) {
        CuratedPhotoModel(
            id = photosDataId,
            url = url,
            photographer = photographer
        )
    }

    fun mapFromModelToPhotosDataEntity(model: CuratedPhotoModel): PhotosDataEntity = with(model) {
        PhotosDataEntity(
            photosDataId = id,
            url = url,
            photographer = photographer
        )
    }

}