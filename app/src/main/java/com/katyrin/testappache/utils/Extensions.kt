package com.katyrin.testappache.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.util.TypedValue
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.katyrin.testappache.R
import com.katyrin.testappache.model.storage.BitmapConverter
import android.view.Display




fun Context.getColorByThemeAttr(attr: Int): Int {
    val typedValue = TypedValue()
    val got: Boolean = theme.resolveAttribute(attr, typedValue, true)
    return if (got) typedValue.data else ContextCompat.getColor(this, R.color.always_white)
}

fun Fragment.getColorByThemeAttr(attr: Int): Int {
    val typedValue = TypedValue()
    val got: Boolean = requireContext().theme.resolveAttribute(attr, typedValue, true)
    return if (got) typedValue.data
    else ContextCompat.getColor(requireContext(), R.color.always_white)
}

fun AppCompatImageView.setImageByUri(
    bitmap: Bitmap,
    placeholder: Int = R.drawable.ic_no_photo_vector
) {
    Glide.with(context)
        .asBitmap()
        .load(BitmapConverter().fromBitmap(bitmap))
        .placeholder(placeholder)
        .error(placeholder)
        .into(this)
}

fun Fragment.toast(message: String?) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}



