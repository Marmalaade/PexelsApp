package com.example.pexelsapp.data.database

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotosDataDataSource @Inject constructor(
    private val photosDataDao: PhotosDataDao
) {

    fun getAllPhotos(): Single<List<PhotosDataEntity>> {
        return Single.fromCallable {
            photosDataDao.getAllPhotos()
        }
            .subscribeOn(Schedulers.io())
    }

    fun insertPhoto(photo: PhotosDataEntity): Completable {
        return Completable.fromAction {
            photosDataDao.insertPhoto(photo)
        }
            .subscribeOn(Schedulers.io())
    }

    fun deletePhoto(photoId: Int): Completable {
        return Completable.fromAction {
            photosDataDao.deletePhoto(photoId)
        }
            .subscribeOn(Schedulers.io())
    }

    fun getPhoto(photoId: Int): Single<PhotosDataEntity?> {
        return Single.fromCallable {
            photosDataDao.getPhoto(photoId)
        }
            .subscribeOn(Schedulers.io())
    }


}