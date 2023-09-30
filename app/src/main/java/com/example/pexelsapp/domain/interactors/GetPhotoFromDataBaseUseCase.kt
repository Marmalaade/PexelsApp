package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.MainRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import io.reactivex.Maybe
import javax.inject.Inject

class GetPhotoFromDataBaseUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    operator fun invoke(photoId: Int): Maybe<CuratedPhotoModel?> = mainRepository.getPhotoFromDataBase(photoId)
}