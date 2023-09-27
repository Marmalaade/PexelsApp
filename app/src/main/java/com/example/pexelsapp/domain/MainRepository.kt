package com.example.pexelsapp.domain

import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import io.reactivex.Completable
import io.reactivex.Single

interface MainRepository {
    fun getPopularRequests(): Single<List<RequestModel>>
    fun getCuratedPhotos(): Single<List<CuratedPhotoModel>>
    fun getSelectedPhoto(id: Int): Single<CuratedPhotoModel>
    fun getPhotosByRequest(query: String): Single<List<CuratedPhotoModel>>
    fun getPhotosFromDataBase(): Single<List<CuratedPhotoModel>>
    fun insertPhotoInDataBase(photo: CuratedPhotoModel): Completable
    fun deletePhotoFromDataBase(photoId: Int): Completable
    fun getPhotoFromDataBase(photoId: Int): Single<CuratedPhotoModel?>


}