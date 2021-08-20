package com.katyrin.testappache.model.repository

import com.katyrin.testappache.model.entities.ContentData

interface LocalRepository {
    suspend fun getSavedProjects(): List<ContentData>
}