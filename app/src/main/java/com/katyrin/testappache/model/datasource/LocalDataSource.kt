package com.katyrin.testappache.model.datasource

import com.katyrin.testappache.model.entities.ContentData

interface LocalDataSource {
    suspend fun getSavedProjects(): List<ContentData>
    suspend fun saveImage(contentData: ContentData)
}