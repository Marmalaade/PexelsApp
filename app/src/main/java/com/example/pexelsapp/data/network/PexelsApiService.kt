package com.example.pexelsapp.data.network

import com.example.pexelsapp.data.apimodels.CuratedPhotoResponse
import com.example.pexelsapp.data.apimodels.CuratedPhotosResponse
import com.example.pexelsapp.data.apimodels.RequestCollectionResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface PexelsApiService {

    @GET("search")
    fun getPhotosByRequest(
        @Header("Authorization") apiKey: String,
        @Query("query") category: String,
        @Query("per_page") perPage: Int,
    ):Single<CuratedPhotosResponse>

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

    @GET("photos/{id}")
    fun getSelectedPhoto(
        @Header("Authorization") apiKey: String,
        @Path("id") id: Int,
    ): Single<CuratedPhotoResponse>

}