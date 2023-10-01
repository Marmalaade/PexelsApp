package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.PhotosRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import javax.inject.Inject

class InsertPhotoInDataBaseUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) {
    operator fun invoke(photo: CuratedPhotoModel) = photosRepository.insertPhotoInDataBase(photo)
}