package com.katyrin.testappache.viewmodel

import com.katyrin.testappache.model.entities.ContentData

sealed class AppState {
    data class Success(val data: List<ContentData>) : AppState()
    data class Error(val message: String?) : AppState()
    object Loading : AppState()
}
