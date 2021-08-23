package com.katyrin.testappache.model.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content")
data class ContentEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "Мой проект",
    val bitmap: Bitmap
)
