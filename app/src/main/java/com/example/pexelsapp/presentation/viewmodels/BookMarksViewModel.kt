package com.example.pexelsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pexelsapp.domain.interactors.MainUseCases
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class BookMarksViewModel @Inject constructor(
    private val mainUseCases: MainUseCases
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _savedPhotosLiveData = MutableLiveData<List<CuratedPhotoModel>>()
    val savedPhotosLiveData: LiveData<List<CuratedPhotoModel>> get() = _savedPhotosLiveData

    fun getSavedPhotosFromDataBase() {
        disposable = mainUseCases.getPhotosFromDataBase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { savedPhotos ->
                    _savedPhotosLiveData.postValue(savedPhotos)
                },
                { error ->

                    println(error.message)
                }
            )
    }
}