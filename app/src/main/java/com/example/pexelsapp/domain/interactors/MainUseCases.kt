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
    fun getSelectedPhotoUseCase(id: Int): Single<CuratedPhotoModel> = mainRepository.getSelectedPhoto(id)
    fun getPhotosByRequest(query: String): Single<List<CuratedPhotoModel>> = mainRepository.getPhotosByRequest(query)
    fun getPhotosFromDataBase(): Single<List<CuratedPhotoModel>> = mainRepository.getPhotosFromDataBase()
    fun insertPhotoInDataBase(photo: CuratedPhotoModel) = mainRepository.insertPhotoInDataBase(photo)
    fun deletePhotoFromDataBase(photoId: Int) = mainRepository.deletePhotoFromDataBase(photoId)
    fun getPhotoFromDataBase(photoId: Int) = mainRepository.getPhotoFromDataBase(photoId)
}
