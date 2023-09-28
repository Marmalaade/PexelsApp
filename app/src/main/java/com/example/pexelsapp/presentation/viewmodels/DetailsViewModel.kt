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
class DetailsViewModel @Inject constructor(
    private val mainUseCases: MainUseCases
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _selectedPhotoLiveData = MutableLiveData<CuratedPhotoModel>()
    val selectedPhotoLiveData: LiveData<CuratedPhotoModel> get() = _selectedPhotoLiveData

    private val _ifPhotoInDataBaseLiveData = MutableLiveData(false)
    val ifPhotoInDataBaseLiveData: LiveData<Boolean> get() = _ifPhotoInDataBaseLiveData
    fun insertPhotosInDB(photo: CuratedPhotoModel) {
        disposable = mainUseCases.insertPhotoInDataBase(photo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _ifPhotoInDataBaseLiveData.postValue(true)
                },
                { error ->
                    println(error.message)
                }
            )
    }

    fun deletePhotoFromDataBase(id: Int) {
        disposable = mainUseCases.deletePhotoFromDataBase(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _ifPhotoInDataBaseLiveData.postValue(false)
                },
                { error ->
                    println(error.message)
                }
            )
    }

    fun getPhotoFromDataBase(selectedPhotoId: Int) {
        disposable = mainUseCases.getPhotoFromDataBase(selectedPhotoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _ifPhotoInDataBaseLiveData.postValue(true)
                },
                { error ->

                    println(error.message)
                }
            )
    }

    fun getSelectedPhoto(id: Int) {
        disposable = mainUseCases.getSelectedPhotoUseCase(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _selectedPhotoLiveData.postValue(result)
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