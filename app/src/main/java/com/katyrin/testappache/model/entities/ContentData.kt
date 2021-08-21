package com.katyrin.testappache.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentData(
    val name: String = ""
) : Parcelable
