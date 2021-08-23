package com.katyrin.testappache.model.interactor

import com.katyrin.testappache.model.entities.ContentData
import com.katyrin.testappache.model.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DrawingInteractorImpl(
    private val localRepository: LocalRepository
): DrawingInteractor {
    override suspend fun saveImage(contentData: ContentData) {
        withContext(Dispatchers.IO) {
            localRepository.saveImage(contentData)
        }
    }
}