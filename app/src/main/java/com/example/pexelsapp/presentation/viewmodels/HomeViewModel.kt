package com.example.pexelsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private val getPopularRequest: MainUseCases,
    private val getCuratedPhotos: MainUseCases
) : ViewModel() {

    private var disposable: Disposable? = null
    private val _requestsLiveData = MutableLiveData<List<RequestModel>>()
    val requestsLiveData: LiveData<List<RequestModel>> get() = _requestsLiveData

    private val _curatedPhotosLiveData = MutableLiveData<List<CuratedPhotoModel>>()
    val curatedPhotosLiveData: LiveData<List<CuratedPhotoModel>> get() = _curatedPhotosLiveData


    init {
        getPopularRequests()
        getCuratedPhotos()
    }

    private fun getCuratedPhotos() {
        disposable = getCuratedPhotos.getCuratedPhotosUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _curatedPhotosLiveData.value = result
                },
                { error ->
                    println(error.message)
                }
            )
    }

    private fun getPopularRequests() {
        disposable = getPopularRequest.getPopularRequestsUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _requestsLiveData.value = result
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



