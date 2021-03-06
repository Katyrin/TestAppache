package com.katyrin.testappache.model.storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.katyrin.testappache.utils.MAX_QUALITY
import java.io.ByteArrayOutputStream

class BitmapConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, MAX_QUALITY, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap =
        BitmapFactory
            .decodeByteArray(byteArray, 0, byteArray.size)
            .copy(Bitmap.Config.ARGB_8888, true)
}