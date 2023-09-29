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
class DetailsViewModel @Inject constructor(
    private val mainUseCases: MainUseCases
) : ViewModel() {

    private var disposable: Disposable? = null

    private val _selectedPhotoLiveData = MutableLiveData<CuratedPhotoModel>()
    val selectedPhotoLiveData: LiveData<CuratedPhotoModel> get() = _selectedPhotoLiveData

    private val _ifPhotoInDataBaseLiveData = MutableLiveData(false)
    val ifPhotoInDataBaseLiveData: LiveData<Boolean> get() = _ifPhotoInDataBaseLiveData

    private val _detailsPhotoLoadingLiveData = MutableLiveData(false)
    val detailsPhotoLoadingLiveData: LiveData<Boolean> get() = _detailsPhotoLoadingLiveData

    private val _loadingProgressLivedata = MutableLiveData(0)
    val loadingProgressLiveData: LiveData<Int> get() = _loadingProgressLivedata
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
        _detailsPhotoLoadingLiveData.postValue(true)
        disposable = mainUseCases.getSelectedPhotoUseCase(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _detailsPhotoLoadingLiveData.postValue(false)
                    _selectedPhotoLiveData.postValue(result)
                },
                { error ->
                    _detailsPhotoLoadingLiveData.postValue(false)
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