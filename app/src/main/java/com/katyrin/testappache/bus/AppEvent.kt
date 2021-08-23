package com.katyrin.testappache.bus

sealed class AppEvent {
    data class UpdateColor(val color: Int) : AppEvent()
    data class UpdateBrushSize(val size: Float) : AppEvent()
}
