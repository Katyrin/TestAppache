package com.katyrin.testappache.utils

import com.katyrin.testappache.model.entities.ContentData
import com.katyrin.testappache.model.entities.ContentEntity

fun mapDataToEntity(contentData: ContentData): ContentEntity =
    ContentEntity(contentData.id, bitmap = contentData.bitmap!!)

fun mapListEntityToListData(entities: List<ContentEntity>): List<ContentData> {
    val listData: MutableList<ContentData> = mutableListOf()
    for (entity in entities) listData.add(mapEntityToData(entity))
    return listData
}

fun mapEntityToData(entity: ContentEntity): ContentData =
    ContentData(entity.bitmap, entity.name, entity.id)