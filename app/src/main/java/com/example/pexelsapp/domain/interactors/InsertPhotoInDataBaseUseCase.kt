package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.MainRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import javax.inject.Inject

class InsertPhotoInDataBaseUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    operator fun invoke(photo: CuratedPhotoModel) = mainRepository.insertPhotoInDataBase(photo)
}