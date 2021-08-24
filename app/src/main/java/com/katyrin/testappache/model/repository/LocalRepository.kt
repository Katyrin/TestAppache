package com.katyrin.testappache.model.repository

import com.katyrin.testappache.model.entities.ContentData

interface LocalRepository {
    suspend fun getSavedProjects(): List<ContentData>
    suspend fun saveImage(contentData: ContentData)
    suspend fun getProjectById(id: Int): ContentData
}