package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.PhotosRepository
import javax.inject.Inject

class DeletePhotoFromDataBaseUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) {
    operator fun invoke(photoId: Int) = photosRepository.deletePhotoFromDataBase(photoId)
}