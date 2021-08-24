package com.katyrin.testappache.model.repository

import com.katyrin.testappache.model.datasource.LocalDataSource
import com.katyrin.testappache.model.entities.ContentData

class LocalRepositoryImpl(
    private val localDataSource: LocalDataSource
) : LocalRepository {

    override suspend fun getSavedProjects(): List<ContentData> = localDataSource.getSavedProjects()

    override suspend fun saveImage(contentData: ContentData) {
        localDataSource.saveImage(contentData)
    }

    override suspend fun getProjectById(id: Int): ContentData = localDataSource.getProjectById(id)
}