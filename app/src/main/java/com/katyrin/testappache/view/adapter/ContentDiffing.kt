package com.katyrin.testappache.view.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.katyrin.testappache.model.entities.ContentData

object ContentDiffing : DiffUtil.ItemCallback<ContentData>() {

    private val payload = Any()

    override fun areItemsTheSame(
        oldItem: ContentData,
        newItem: ContentData
    ): Boolean = oldItem.id == newItem.id && oldItem.bitmap == newItem.bitmap

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: ContentData,
        newItem: ContentData
    ): Boolean = oldItem == newItem

    override fun getChangePayload(oldItem: ContentData, newItem: ContentData) = payload
}