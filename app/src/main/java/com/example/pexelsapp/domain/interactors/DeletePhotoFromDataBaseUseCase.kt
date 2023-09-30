package com.example.pexelsapp.domain.interactors

import com.example.pexelsapp.domain.MainRepository
import javax.inject.Inject

class DeletePhotoFromDataBaseUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    operator fun invoke(photoId: Int) = mainRepository.deletePhotoFromDataBase(photoId)
}