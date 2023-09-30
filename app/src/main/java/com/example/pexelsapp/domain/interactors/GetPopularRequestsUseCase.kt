package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.MainRepository
import com.example.pexelsapp.domain.models.RequestModel
import io.reactivex.Single
import javax.inject.Inject

class GetPopularRequestsUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    operator fun invoke(): Single<List<RequestModel>> = mainRepository.getPopularRequests()
}