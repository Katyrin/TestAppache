package com.katyrin.testappache.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stroke(
    var strokeWidth: Int,
    var path: ParcelPath,
    var color: Int
) : Parcelable