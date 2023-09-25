package com.example.pexelsapp.domain

import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import io.reactivex.Single

interface MainRepository {
    fun getPopularRequests(): Single<List<RequestModel>>
    fun getCuratedPhotos(): Single<List<CuratedPhotoModel>>
}