package com.katyrin.testappache.model.interactor

import com.katyrin.testappache.model.entities.ContentData
import com.katyrin.testappache.model.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeInteractorImpl(
    private val localRepository: LocalRepository
) : HomeInteractor {
    override suspend fun getSavedProjects(): List<ContentData> =
        withContext(Dispatchers.IO) { localRepository.getSavedProjects() }
}