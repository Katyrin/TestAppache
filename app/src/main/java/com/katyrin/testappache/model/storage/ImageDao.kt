package com.katyrin.testappache.model.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.katyrin.testappache.model.entities.ContentEntity

@Dao
interface ImageDao {

    @Query("SELECT * FROM content")
    suspend fun getAllProjects(): List<ContentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveImage(contentEntity: ContentEntity)

    @Query("SELECT * FROM content WHERE id LIKE :id")
    suspend fun getProjectById(id: Int): ContentEntity
}