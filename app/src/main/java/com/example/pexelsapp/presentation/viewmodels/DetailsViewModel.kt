package com.example.pexelsapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pexelsapp.domain.interactors.DeletePhotoFromDataBaseUseCase
import com.example.pexelsapp.domain.interactors.GetPhotoFromDataBaseUseCase
import com.example.pexelsapp.domain.interactors.GetSelectedPhotoUseCase
import com.example.pexelsapp.domain.interactors.InsertPhotoInDataBaseUseCase
import com.example.pexelsapp.domain.models.CuratedPhotoModel
import com.example.pexelsapp.presentation.genericutils.ApiHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getSelectedPhotoUseCase: GetSelectedPhotoUseCase,
    private val insertPhotoInDataBaseUseCase: InsertPhotoInDataBaseUseCase,
    private val deletePhotoFromDataBaseUseCase: DeletePhotoFromDataBaseUseCase,
    private val getPhotoFromDataBaseUseCase: GetPhotoFromDataBaseUseCase
) : ViewModel() {

    private var disposables = CompositeDisposable()

    private val _selectedPhotoLiveData = MutableLiveData<CuratedPhotoModel>()
    val selectedPhotoLiveData: LiveData<CuratedPhotoModel> get() = _selectedPhotoLiveData

    private val _ifPhotoInDataBaseLiveData = MutableLiveData(false)
    val ifPhotoInDataBaseLiveData: LiveData<Boolean> get() = _ifPhotoInDataBaseLiveData

    private val _detailsPhotoLoadingLiveData = MutableLiveData(false)
    val detailsPhotoLoadingLiveData: LiveData<Boolean> get() = _detailsPhotoLoadingLiveData

    private val _loadingProgressLivedata = MutableLiveData(0)
    val loadingProgressLiveData: LiveData<Int> get() = _loadingProgressLivedata
    fun insertPhotosInDB(photo: CuratedPhotoModel) {
        val disposable = insertPhotoInDataBaseUseCase.invoke(photo)
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
        disposables.add(disposable)
    }

    fun deletePhotoFromDataBase(id: Int) {
        val disposable = deletePhotoFromDataBaseUseCase.invoke(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _ifPhotoInDataBaseLiveData.postValue(false)
                },
                { error ->
                    println(error.message)
                }
            )
        disposables.add(disposable)
    }

    fun getPhotoFromDataBase(selectedPhotoId: Int) {
        val disposable = getPhotoFromDataBaseUseCase.invoke(selectedPhotoId)
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
        disposables.add(disposable)
    }


    fun getSelectedPhoto(id: Int) {
        ApiHandler.handleApiCall(
            _detailsPhotoLoadingLiveData,
            MutableLiveData(),
            _loadingProgressLivedata,
            { result -> _selectedPhotoLiveData.postValue(result) },
            { getSelectedPhotoUseCase.invoke(id).toObservable() },
            disposables
        )
    }


    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}