package com.katyrin.testappache.model.datasource

import com.katyrin.testappache.model.entities.ContentData
import com.katyrin.testappache.model.storage.ImageDao
import com.katyrin.testappache.utils.EMPTY_NAME
import com.katyrin.testappache.utils.mapDataToEntity
import com.katyrin.testappache.utils.mapListEntityToListData

class LocalDataSourceImpl(
    private val imageDao: ImageDao
) : LocalDataSource {

    override suspend fun getSavedProjects(): List<ContentData> {
        val listData: MutableList<ContentData> = mutableListOf(ContentData(name = EMPTY_NAME))
        listData.addAll(mapListEntityToListData(imageDao.getAllProjects()))
        return listData
    }

    override suspend fun saveImage(contentData: ContentData) {
        imageDao.saveImage(mapDataToEntity(contentData))
    }
}