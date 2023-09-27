package com.example.pexelsapp.data

import android.util.Log
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.data.database.PhotosDataDataSource
import com.example.pexelsapp.data.mappers.DataMapper
import com.example.pexelsapp.data.network.PexelsApiService
import com.example.pexelsapp.domain.MainRepository
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainRepositoryImpl @Inject constructor(
    private val apiService: PexelsApiService,
    private val mapper: DataMapper,
    private val photosDataDataSource: PhotosDataDataSource
) : MainRepository {

    override fun getPopularRequests(): Single<List<RequestModel>> {
        return apiService.getFeaturedCollections(AppConfig.getApiKey(), AppConfig.getRequestsPerPage())
            .map { response ->
                response.collections?.map {
                    mapper.mapToRequestModel(it)
                } ?: emptyList()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getCuratedPhotos(): Single<List<CuratedPhotoModel>> {
        return apiService.getCuratedPhotos(AppConfig.getApiKey(), AppConfig.getPhotosPerPage())
            .map { response ->
                response.photos?.map {
                    mapper.mapToCuratedPhotoModel(it)
                } ?: emptyList()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getSelectedPhoto(id: Int): Single<CuratedPhotoModel> {
        return apiService.getSelectedPhoto(AppConfig.getApiKey(), id)
            .map { response ->
                mapper.mapToCuratedPhotoModel(response)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getPhotosByRequest(query: String): Single<List<CuratedPhotoModel>> {
        return apiService.getPhotosByRequest(AppConfig.getApiKey(), query, AppConfig.getPhotosPerPage())
            .map { response ->
                response.photos?.map {
                    mapper.mapToCuratedPhotoModel(it)
                } ?: emptyList()
            }
            .subscribeOn(Schedulers.io())
    }

    override fun getPhotosFromDataBase(): Single<List<CuratedPhotoModel>> {
        return photosDataDataSource.getAllPhotos()
            .map { photosDataList ->
                photosDataList.map { photosDataEntity ->
                    mapper.mapFromPhotosDataEntityToModel(photosDataEntity)
                }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun insertPhotoInDataBase(photo: CuratedPhotoModel): Completable {
        return Completable.fromAction {
            Log.e("insert", "$photo")
            photosDataDataSource.insertPhoto(mapper.mapFromModelToPhotosDataEntity(photo))
        }
            .subscribeOn(Schedulers.io())
    }

    override fun deletePhotoFromDataBase(photoId: Int): Completable {
        return Completable.fromAction {
            Log.e("delete", "$photoId")
            photosDataDataSource.deletePhoto(photoId)
        }
            .subscribeOn(Schedulers.io())
    }

    override fun getPhotoFromDataBase(photoId: Int): Maybe<CuratedPhotoModel?> {
        Log.e("get", "$photoId")
        return photosDataDataSource.getPhoto(photoId)
            .map { photosDataEntity ->
                mapper.mapFromPhotosDataEntityToModel(photosDataEntity)
            }
            .subscribeOn(Schedulers.io())
    }


}