package com.example.pexelsapp.presentation.viewmodels

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pexelsapp.common.AppConfig
import com.example.pexelsapp.domain.interactors.MainUseCases
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.domain.models.RequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainUseCases: MainUseCases
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

    private val _loadingProgressLivedata = MutableLiveData(0)
    val loadingProgressLiveData: LiveData<Int> get() = _loadingProgressLivedata

    fun setCurrentQuery(query: String) {
        currentQuery = query
    }

    fun getCurrentQuery() = currentQuery

    private fun <T> handleApiCall(
        loadingLiveData: MutableLiveData<Boolean>,
        networkErrorLiveData: MutableLiveData<Boolean>,
        successCallback: (T) -> Unit,
        apiCall: () -> Observable<T>
    ) {
        loadingLiveData.postValue(true)
        networkErrorLiveData.postValue(false)

        val disposable = apiCall.invoke()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    loadingLiveData.postValue(false)
                    successCallback(result)
                },
                { error ->
                    when (error) {
                        is UnknownHostException,
                        is SocketTimeoutException,
                        is NetworkErrorException -> {
                            networkErrorLiveData.postValue(true)
                        }

                        else -> {
                            loadingLiveData.postValue(false)
                            println(error.message)
                        }
                    }
                }
            )

        disposables.add(disposable)

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
                disposables.add(it)
            }
    }

    fun getPhotosByRequest(query: String) {
        handleApiCall(
            _homePhotosLoadingLiveData,
            _photosLoadingNetworkErrorLiveData,
            { _photosByRequestLiveData.postValue(it) }
        ) {
            mainUseCases.getPhotosByRequest(query).toObservable()
        }
    }

    fun getCuratedPhotos() {
        handleApiCall(
            _homePhotosLoadingLiveData,
            _photosLoadingNetworkErrorLiveData,
            { _curatedPhotosLiveData.postValue(it) }
        ) {
            mainUseCases.getCuratedPhotosUseCase().toObservable()
        }
    }

    fun getPopularRequests() {
        handleApiCall(
            _homePhotosLoadingLiveData,
            _photosLoadingNetworkErrorLiveData,
            { _popularRequestsLiveData.postValue(it) }
        ) {
            mainUseCases.getPopularRequestsUseCase().toObservable()
        }
    }


    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}
