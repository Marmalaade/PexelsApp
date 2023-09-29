package com.example.pexelsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pexelsapp.domain.interactors.MainUseCases
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class BookMarksViewModel @Inject constructor(
    private val mainUseCases: MainUseCases
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _savedPhotosLiveData = MutableLiveData<List<CuratedPhotoModel>>()
    val savedPhotosLiveData: LiveData<List<CuratedPhotoModel>> get() = _savedPhotosLiveData

    private val _savedPhotosLoadingLiveData = MutableLiveData(false)
    val savedPhotosLoadingLiveData: LiveData<Boolean> get() = _savedPhotosLoadingLiveData

    private val _loadingProgressLivedata = MutableLiveData(0)
    val loadingProgressLiveData: LiveData<Int> get() = _loadingProgressLivedata

    fun getSavedPhotosFromDataBase() {
        _savedPhotosLoadingLiveData.postValue(true)

        disposable = mainUseCases.getPhotosFromDataBase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { savedPhotos ->
                    _savedPhotosLoadingLiveData.postValue(false)
                    _savedPhotosLiveData.postValue(savedPhotos)
                },
                { error ->
                    _savedPhotosLoadingLiveData.postValue(false)
                    println(error.message)
                }
            )

        Observable.interval(30.toLong(), TimeUnit.MILLISECONDS)
            .take(100.toLong())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { step ->
                    val currentProgress = ((step + 1) * 1)
                    _loadingProgressLivedata.postValue(currentProgress.toInt())
                },
                { error ->
                    println(error.message)
                }
            )
            .let {
                disposable = it
            }
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}