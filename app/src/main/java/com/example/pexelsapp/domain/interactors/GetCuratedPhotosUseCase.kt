package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.MainRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import io.reactivex.Single
import javax.inject.Inject

class GetCuratedPhotosUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    operator fun invoke(): Single<List<CuratedPhotoModel>> = mainRepository.getCuratedPhotos()
}