package com.katyrin.testappache.viewmodel

sealed class DrawingState {
    object Success : DrawingState()
    data class Error(val message: String?) : DrawingState()
}