package com.example.pexelsapp.data.network

import com.example.pexelsapp.data.apimodels.CuratedPhotosResponse
import com.example.pexelsapp.data.apimodels.RequestCollectionResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApiService {

    @GET("collections/featured")
    fun getFeaturedCollections(
        @Header("Authorization") apiKey: String,
        @Query("per_page") perPage: Int
    ): Single<RequestCollectionResponse>

    @GET("curated")
    fun getCuratedPhotos(
        @Header("Authorization") apiKey: String,
        @Query("per_page") perPage: Int,
    ): Single<CuratedPhotosResponse>

}