package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.MainRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import io.reactivex.Single
import javax.inject.Inject

class GetSelectedPhotoUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    operator fun invoke(id: Int): Single<CuratedPhotoModel> = mainRepository.getSelectedPhoto(id)
}