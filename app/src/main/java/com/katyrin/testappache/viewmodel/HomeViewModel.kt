package com.katyrin.testappache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.testappache.model.interactor.HomeInteractor
import kotlinx.coroutines.*

class HomeViewModel(
    private val homeInteractor: HomeInteractor
) : ViewModel() {

    private val _mutableLiveData: MutableLiveData<AppState> = MutableLiveData()
    private val liveData: LiveData<AppState> = _mutableLiveData

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
    )

    private fun handleError(error: Throwable) {
        _mutableLiveData.postValue(AppState.Error(error.message))
    }

    fun subscribe(): LiveData<AppState> = liveData

    fun getSavedProjects() {
        _mutableLiveData.value = AppState.Loading
        cancelJob()
        viewModelCoroutineScope.launch {
            _mutableLiveData.postValue(AppState.Success(homeInteractor.getSavedProjects()))
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    private fun cancelJob(): Unit = viewModelCoroutineScope.coroutineContext.cancelChildren()
}