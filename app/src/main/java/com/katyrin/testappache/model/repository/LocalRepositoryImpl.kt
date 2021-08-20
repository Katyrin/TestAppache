package com.katyrin.testappache.model.repository

import com.katyrin.testappache.model.entities.ContentData
import com.katyrin.testappache.model.datasource.LocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRepositoryImpl(
    private val localDataSource: LocalDataSource
) : LocalRepository {

    override suspend fun getSavedProjects(): List<ContentData> =
        withContext(Dispatchers.IO) {
            localDataSource.getSavedProjects()
        }
}