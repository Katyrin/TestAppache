package com.katyrin.testappache.model.interactor

import android.net.Uri
import com.katyrin.testappache.model.entities.ContentData

interface DrawingInteractor {
    suspend fun saveImage(contentData: ContentData)
    suspend fun getProjectById(id: Int): ContentData
    suspend fun getUri(contentData: ContentData): Uri
}