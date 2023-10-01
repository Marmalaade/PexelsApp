package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.PhotosRepository
import com.example.pexelsapp.domain.models.RequestModel
import io.reactivex.Single
import javax.inject.Inject

class GetPopularRequestsUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) {
    operator fun invoke(): Single<List<RequestModel>> = photosRepository.getPopularRequests()
}