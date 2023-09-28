package com.example.pexelsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.domain.interactors.MainUseCases
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainUseCases: MainUseCases
) : ViewModel() {

    private var disposable: Disposable? = null

    private var currentQuery = AppConfig.getBaseRequest()

    private val _popularRequestsLiveData = MutableLiveData<List<RequestModel>>()
    val popularRequestsLiveData: LiveData<List<RequestModel>> get() = _popularRequestsLiveData

    private val _curatedPhotosLiveData = MutableLiveData<List<CuratedPhotoModel>>()
    val curatedPhotosLiveData: LiveData<List<CuratedPhotoModel>> get() = _curatedPhotosLiveData

    private val _photosByRequestLiveData = MutableLiveData<List<CuratedPhotoModel>>()
    val photosByRequestLiveData: LiveData<List<CuratedPhotoModel>> get() = _photosByRequestLiveData


    fun setCurrentQuery(query: String) {
        currentQuery = query
    }

    fun getCurrentQuery() = currentQuery

    fun getPhotosByRequest(query: String) {
        disposable = mainUseCases.getPhotosByRequest(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _photosByRequestLiveData.postValue(result)
                },
                { error ->
                    println(error.message)
                }
            )

    }

    fun getCuratedPhotos() {
        disposable = mainUseCases.getCuratedPhotosUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _curatedPhotosLiveData.postValue(result)
                },
                { error ->
                    println(error.message)
                }
            )
    }

    fun getPopularRequests() {
        disposable = mainUseCases.getPopularRequestsUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _popularRequestsLiveData.postValue(result)
                },
                { error ->
                    println(error.message)
                }
            )
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }


}
