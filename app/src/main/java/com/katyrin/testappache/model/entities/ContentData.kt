package com.katyrin.testappache.model.entities

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentData(
    var bitmap: Bitmap? = null,
    val name: String = "",
    val id: Int = 0
) : Parcelable
