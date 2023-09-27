package com.example.pexelsapp.presentation.viewmodels

import android.util.Log
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

    private val _selectedPhotoLiveData = MutableLiveData<CuratedPhotoModel>()
    val selectedPhotoLiveData: LiveData<CuratedPhotoModel> get() = _selectedPhotoLiveData

    private val _photosByRequestLiveData = MutableLiveData<List<CuratedPhotoModel>>()
    val photosByRequestLiveData: LiveData<List<CuratedPhotoModel>> get() = _photosByRequestLiveData

    private val _networkErrorLiveData = MutableLiveData<Boolean>()
    val networkErrorLiveData: LiveData<Boolean> get() = _networkErrorLiveData

    private val _dataLoadingLiveData = MutableLiveData<Boolean>()
    val dataLoadingLiveData: LiveData<Boolean> get() = _dataLoadingLiveData

    private val _dataLoadingProgressLiveData = MutableLiveData<Int>()
    val dataLoadingProgressLiveData: LiveData<Int> get() = _dataLoadingProgressLiveData

    private val _ifPhotoInDataBaseLiveData = MutableLiveData<Boolean>(false)
    val ifPhotoInDataBaseLiveData: LiveData<Boolean> get() = _ifPhotoInDataBaseLiveData
    private val _mama = MutableLiveData<List<CuratedPhotoModel>>()
    val mama: LiveData<List<CuratedPhotoModel>> get() = _mama


    fun setCurrentQuery(query: String) {
        currentQuery = query
    }

    fun getCurrentQuery() = currentQuery

    fun getPhotosByRequest(query: String) {
        _networkErrorLiveData.value = false
        disposable = mainUseCases.getPhotosByRequest(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _photosByRequestLiveData.value = result
                },
                { error ->
                    _networkErrorLiveData.value = true
                    println(error.message)
                }
            )
        _dataLoadingLiveData.value = false

    }

    fun getSelectedPhoto(id: Int) {
        _networkErrorLiveData.value = false
        disposable = mainUseCases.getSelectedPhotoUseCase(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _selectedPhotoLiveData.value = result
                },
                { error ->
                    _networkErrorLiveData.value = true
                    println(error.message)
                }
            )
        _dataLoadingLiveData.value = false

    }

    fun getCuratedPhotos() {
        _networkErrorLiveData.value = false
        _dataLoadingLiveData.value = true
        disposable = mainUseCases.getCuratedPhotosUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _curatedPhotosLiveData.value = result
                },
                { error ->
                    _networkErrorLiveData.value = true
                    println(error.message)
                }
            )
        _dataLoadingLiveData.value = false
    }

    fun getPopularRequests() {
        _networkErrorLiveData.value = false
        _dataLoadingLiveData.value = true
        disposable = mainUseCases.getPopularRequestsUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _popularRequestsLiveData.value = result
                },
                { error ->
                    _networkErrorLiveData.value = true
                    println(error.message)
                }
            )
        _dataLoadingLiveData.value = false
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun getAllPhotosFromDataBase() {
        disposable = mainUseCases.getPhotosFromDataBase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    _mama.value = result
                    Log.e("resulttt","$result")
                },
                { error ->
                    println(error.message)
                }
            )
    }

    fun insertPhotosInDB(photo: CuratedPhotoModel) {
        disposable = mainUseCases.insertPhotoInDataBase(photo)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.e("insertsection", "Фотография успешно вставлена.")
                    _ifPhotoInDataBaseLiveData.value = true
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
                    Log.e("deletinsection", "Фотография успешно уделана.")
                    _ifPhotoInDataBaseLiveData.value = false
                },
                { error ->
                    println(error.message)
                }
            )
    }

    fun getPhotoFromDataBase(selectedPhotoId: Int) {
        Log.e("Method", "getPhotoFromDataBase")
        disposable = mainUseCases.getPhotoFromDataBase(selectedPhotoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.e("result", "$result")
                    _ifPhotoInDataBaseLiveData.value = true
                    _selectedPhotoLiveData.value = result
                },
                { error ->
                    Log.e("error", "${error.message}")
                    _networkErrorLiveData.value = true
                    println(error.message)
                }
            )
        _dataLoadingLiveData.value = false
    }
}
