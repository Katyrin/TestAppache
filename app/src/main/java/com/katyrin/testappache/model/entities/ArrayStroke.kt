package com.katyrin.testappache.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.ArrayList

@Parcelize
data class ArrayStroke(
    val strokes: @RawValue ArrayList<Stroke> = arrayListOf(),
    val previousStrokes: @RawValue ArrayList<Stroke> = arrayListOf()
) : Parcelable