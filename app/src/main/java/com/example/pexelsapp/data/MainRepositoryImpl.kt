package com.example.pexelsapp.data

import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.data.mappers.DataMapper
import com.example.pexelsapp.data.network.PexelsApiService
import com.example.pexelsapp.domain.MainRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import io.reactivex.Single
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    private val apiService: PexelsApiService,
    private val mapper: DataMapper
) : MainRepository {

    override fun getPopularRequests(): Single<List<RequestModel>> {
        return apiService.getFeaturedCollections(AppConfig.getApiKey(), AppConfig.getRequestsPerPage())
            .map { response ->
                response.collections?.map {
                    mapper.mapToRequestModel(it)
                } ?: emptyList()
            }
    }

    override fun getCuratedPhotos(): Single<List<CuratedPhotoModel>> {
        return apiService.getCuratedPhotos(AppConfig.getApiKey(), AppConfig.getPhotosPerPage())
            .map { response ->
                response.photos?.map {
                    mapper.mapToCuratedPhotoModel(it)
                } ?: emptyList()
            }
    }

    override fun getSelectedPhoto(id: Int): Single<CuratedPhotoModel> {
        return apiService.getSelectedPhoto(AppConfig.getApiKey(), id)
            .map { response ->
                mapper.mapToCuratedPhotoModel(response)
            }
    }

    override fun getPhotosByRequest(query: String): Single<List<CuratedPhotoModel>> {
        return apiService.getPhotosByRequest(AppConfig.getApiKey(), query, AppConfig.getPhotosPerPage())
            .map { response ->
                response.photos?.map {
                    mapper.mapToCuratedPhotoModel(it)
                } ?: emptyList()

            }
    }
}