package com.example.pexelsapp.data.database

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
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

    fun getPhoto(photoId: Int): Maybe<PhotosDataEntity> {
        return Maybe.create { emitter ->
            val photo = photosDataDao.getPhoto(photoId)
            if (photo != null) {
                emitter.onSuccess(photo)
            } else {
                emitter.onComplete()
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
