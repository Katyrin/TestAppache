package com.katyrin.testappache.model.interactor

import com.katyrin.testappache.model.entities.ContentData

interface DrawingInteractor {
    suspend fun saveImage(contentData: ContentData)
    suspend fun getProjectById(id: Int): ContentData
}