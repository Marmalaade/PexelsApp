package com.example.pexelsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.domain.interactors.GetCuratedPhotosUseCase
import com.example.pexelsapp.domain.interactors.GetPhotosByRequestUseCase
import com.example.pexelsapp.domain.interactors.GetPopularRequestsUseCase
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import com.example.pexelsapp.presentation.generics.ApiHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPhotosByRequestUseCase: GetPhotosByRequestUseCase,
    private val getPopularRequestsUseCase: GetPopularRequestsUseCase,
    private val getCuratedPhotosUseCase: GetCuratedPhotosUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private var currentQuery = AppConfig.getBaseRequest()

    private val _popularRequestsLiveData = MutableLiveData<List<RequestModel>>()
    val popularRequestsLiveData: LiveData<List<RequestModel>> get() = _popularRequestsLiveData

    private val _curatedPhotosLiveData = MutableLiveData<List<CuratedPhotoModel>>()
    val curatedPhotosLiveData: LiveData<List<CuratedPhotoModel>> get() = _curatedPhotosLiveData

    private val _photosByRequestLiveData = MutableLiveData<List<CuratedPhotoModel>>()
    val photosByRequestLiveData: LiveData<List<CuratedPhotoModel>> get() = _photosByRequestLiveData

    private val _photosLoadingNetworkErrorLiveData = MutableLiveData(false)
    val photosLoadingNetworkErrorLiveData: LiveData<Boolean> get() = _photosLoadingNetworkErrorLiveData

    private val _homePhotosLoadingLiveData = MutableLiveData(false)
    val homePhotosLoadingLiveData: LiveData<Boolean> get() = _homePhotosLoadingLiveData

    private val _photosLoadingProgressLiveData = MutableLiveData(0)
    val photosLoadingProgressLiveData: LiveData<Int> get() = _photosLoadingProgressLiveData

    fun setCurrentQuery(query: String) {
        currentQuery = query
    }

    fun getCurrentQuery() = currentQuery


    fun getPhotosByRequest(query: String) {
        ApiHandler.handleApiCall(
            _homePhotosLoadingLiveData,
            _photosLoadingNetworkErrorLiveData,
            _photosLoadingProgressLiveData,
            { _photosByRequestLiveData.postValue(it) },
            { getPhotosByRequestUseCase.invoke(query).toObservable() },
            disposables
        )
    }

    fun getCuratedPhotos() {
        ApiHandler.handleApiCall(
            _homePhotosLoadingLiveData,
            _photosLoadingNetworkErrorLiveData,
            _photosLoadingProgressLiveData,
            { _curatedPhotosLiveData.postValue(it) },
            { getCuratedPhotosUseCase.invoke().toObservable() },
            disposables
        )
    }

    fun getPopularRequests() {
        ApiHandler.handleApiCall(
            _homePhotosLoadingLiveData,
            _photosLoadingNetworkErrorLiveData,
            _photosLoadingProgressLiveData,
            { _popularRequestsLiveData.postValue(it) },
            { getPopularRequestsUseCase.invoke().toObservable() },
            disposables
        )
    }


    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}
