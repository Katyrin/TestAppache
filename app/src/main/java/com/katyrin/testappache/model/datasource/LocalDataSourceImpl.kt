package com.katyrin.testappache.model.datasource

import com.katyrin.testappache.model.entities.ContentData
import com.katyrin.testappache.model.storage.ImageDao
import com.katyrin.testappache.utils.EMPTY_NAME
import com.katyrin.testappache.utils.mapDataToEntity
import com.katyrin.testappache.utils.mapEntityToData
import com.katyrin.testappache.utils.mapListEntityToListData

class LocalDataSourceImpl(
    private val imageDao: ImageDao
) : LocalDataSource {

    override suspend fun getSavedProjects(): List<ContentData> =
        mutableListOf(ContentData(name = EMPTY_NAME)).apply {
            addAll(mapListEntityToListData(imageDao.getAllProjects()))
        }

    override suspend fun saveImage(contentData: ContentData): Unit =
        imageDao.saveImage(mapDataToEntity(contentData))

    override suspend fun getProjectById(id: Int): ContentData =
        mapEntityToData(imageDao.getProjectById(id))
}