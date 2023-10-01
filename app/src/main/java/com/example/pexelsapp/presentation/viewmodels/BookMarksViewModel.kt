package com.example.pexelsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pexelsapp.domain.interactors.GetPhotosFromDataBaseUseCase
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.presentation.genericutils.ApiHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class BookMarksViewModel @Inject constructor(
    private val getPhotosFromDataBaseUseCase: GetPhotosFromDataBaseUseCase
) : ViewModel() {

    private var disposables = CompositeDisposable()

    private val _savedPhotosLiveData = MutableLiveData<List<CuratedPhotoModel>>()
    val savedPhotosLiveData: LiveData<List<CuratedPhotoModel>> get() = _savedPhotosLiveData

    private val _savedPhotosLoadingLiveData = MutableLiveData(false)
    val savedPhotosLoadingLiveData: LiveData<Boolean> get() = _savedPhotosLoadingLiveData

    private val _loadingProgressLivedata = MutableLiveData(0)
    val loadingProgressLiveData: LiveData<Int> get() = _loadingProgressLivedata

    fun getSavedPhotosFromDataBase() {
        ApiHandler.handleApiCall(
            _savedPhotosLoadingLiveData,
            MutableLiveData(),
            _loadingProgressLivedata,
            { savedPhotos -> _savedPhotosLiveData.postValue(savedPhotos) },
            { getPhotosFromDataBaseUseCase.invoke().toObservable() },
            disposables
        )
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}