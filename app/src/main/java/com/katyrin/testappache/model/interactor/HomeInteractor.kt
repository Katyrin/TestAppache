package com.katyrin.testappache.model.interactor

import com.katyrin.testappache.model.entities.ContentData

interface HomeInteractor {
    suspend fun getSavedProjects(): List<ContentData>
}