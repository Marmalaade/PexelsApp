package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.MainRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import io.reactivex.Single
import javax.inject.Inject

class MainUseCases @Inject constructor(
    private val mainRepository: MainRepository
) {
    fun getPopularRequestsUseCase(): Single<List<RequestModel>> = mainRepository.getPopularRequests()

    fun getCuratedPhotosUseCase(): Single<List<CuratedPhotoModel>> = mainRepository.getCuratedPhotos()
}