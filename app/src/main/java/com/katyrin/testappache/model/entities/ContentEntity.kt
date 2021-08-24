package com.katyrin.testappache.model.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.katyrin.testappache.utils.MY_PROJECT_NAME

@Entity(tableName = "content")
data class ContentEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = MY_PROJECT_NAME,
    val bitmap: Bitmap
)