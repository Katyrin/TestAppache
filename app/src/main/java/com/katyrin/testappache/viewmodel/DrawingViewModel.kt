package com.katyrin.testappache.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.katyrin.testappache.model.entities.ContentData
import com.katyrin.testappache.model.interactor.DrawingInteractor
import kotlinx.coroutines.*

class DrawingViewModel(
    private val drawingInteractor: DrawingInteractor
) : ViewModel() {

    private val _mutableLiveData: MutableLiveData<DrawingState> = MutableLiveData()
    private val liveData: LiveData<DrawingState> = _mutableLiveData

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
    )

    private fun handleError(error: Throwable) {
        _mutableLiveData.postValue(DrawingState.Error(error.message))
    }

    fun subscribe(): LiveData<DrawingState> = liveData

    fun saveImage(contentData: ContentData) {
        cancelJob()
        viewModelCoroutineScope.launch {
            drawingInteractor.saveImage(contentData)
            _mutableLiveData.postValue(DrawingState.SuccessSave)
        }
    }

    fun getProjectById(id: Int) {
        cancelJob()
        viewModelCoroutineScope.launch {
            val contentData = drawingInteractor.getProjectById(id)
            _mutableLiveData.postValue(DrawingState.Success(contentData))
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    private fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }
}