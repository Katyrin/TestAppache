package com.katyrin.testappache.model.datasource

import com.katyrin.testappache.model.entities.ContentData

class LocalDataSourceImpl : LocalDataSource {

    private var fakeDataList: MutableList<ContentData> = mutableListOf(
        ContentData("First"),
        ContentData("Second"),
        ContentData("Third"),
        ContentData("Fourth"),
        ContentData("Fifth"),
        ContentData("Sixth"),
        ContentData("Seventh")
    )

    override suspend fun getSavedProjects(): List<ContentData> {
        fakeDataList.add(0, ContentData("Empty"))
        return fakeDataList
    }
}