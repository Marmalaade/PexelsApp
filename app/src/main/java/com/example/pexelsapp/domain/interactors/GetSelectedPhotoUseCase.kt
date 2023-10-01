package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.PhotosRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import io.reactivex.Single
import javax.inject.Inject

class GetSelectedPhotoUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) {
    operator fun invoke(id: Int): Single<CuratedPhotoModel> = photosRepository.getSelectedPhoto(id)
}