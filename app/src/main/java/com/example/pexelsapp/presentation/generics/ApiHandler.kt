package com.example.pexelsapp.presentation.generics

import android.accounts.NetworkErrorException
import androidx.lifecycle.MutableLiveData
import com.example.pexelsapp.common.AppConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

object ApiHandler {
    fun <T> handleApiCall(
        loadingLiveData: MutableLiveData<Boolean>,
        networkErrorLiveData: MutableLiveData<Boolean>,
        loadingProgressLivedata: MutableLiveData<Int>,
        successCallback: (T) -> Unit,
        apiCall: () -> Observable<T>,
        disposables: CompositeDisposable
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

        Observable.interval(AppConfig.getProgressDelay().toLong(), TimeUnit.MILLISECONDS)
            .take(AppConfig.getMaxProgress().toLong())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { step ->
                    val currentProgress = ((step + 1))
                    loadingProgressLivedata.postValue(currentProgress.toInt())
                },
                { error ->
                    println(error.message)
                }
            )
            .let {
                disposables.add(it)
            }
    }
}