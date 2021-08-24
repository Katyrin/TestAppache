package com.katyrin.testappache.viewmodel

import android.net.Uri
import com.katyrin.testappache.model.entities.ContentData

sealed class DrawingState {
    object SuccessSave : DrawingState()
    data class Success(val contentData: ContentData) : DrawingState()
    data class SuccessShare(val contentUri: Uri) : DrawingState()
    data class Error(val message: String?) : DrawingState()
}