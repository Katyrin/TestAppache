package com.katyrin.testappache.utils

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.katyrin.testappache.R

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