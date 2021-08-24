package com.katyrin.testappache.model.uri

import android.net.Uri
import com.katyrin.testappache.model.entities.ContentData

interface UriGenerator {
    suspend fun getUri(contentData: ContentData): Uri
}