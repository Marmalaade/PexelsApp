package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.PhotosRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import io.reactivex.Maybe
import javax.inject.Inject

class GetPhotoFromDataBaseUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) {
    operator fun invoke(photoId: Int): Maybe<CuratedPhotoModel?> = photosRepository.getPhotoFromDataBase(photoId)
}