package com.katyrin.testappache.model.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.katyrin.testappache.model.entities.ContentEntity

@Database(entities = [ContentEntity::class], version = 1, exportSchema = false)
@TypeConverters(BitmapConverter::class)
abstract class ContentDataBase : RoomDatabase() {
    abstract fun getImageDao(): ImageDao
}