package com.katyrin.testappache.model.uri

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.katyrin.testappache.model.entities.ContentData
import com.katyrin.testappache.utils.MAX_QUALITY
import com.katyrin.testappache.utils.MY_PROJECT_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class UriGeneratorImpl(
    private val context: Context
) : UriGenerator {

    override suspend fun getUri(contentData: ContentData): Uri {
        saveImage(contentData)
        return getFileProviderUri(contentData)
    }

    private suspend fun saveImage(contentData: ContentData): Unit =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val cachePath = File(context.cacheDir, IMAGES).apply { mkdirs() }
                FileOutputStream("$cachePath/$MY_PROJECT_NAME${contentData.id}.png")
                    .use { stream ->
                        contentData.bitmap?.compress(Bitmap.CompressFormat.PNG, MAX_QUALITY, stream)
                    }
            }
        }

    private suspend fun getFileProviderUri(contentData: ContentData): Uri =
        withContext(Dispatchers.IO) {
            val imagePath = File(context.cacheDir, IMAGES)
            val newFile = File(imagePath, "$MY_PROJECT_NAME${contentData.id}.png")
            FileProvider.getUriForFile(context, AUTHORITY, newFile)
        }

    private companion object {
        const val IMAGES = "images"
        const val AUTHORITY = "com.katyrin.testappache.provider"
    }
}