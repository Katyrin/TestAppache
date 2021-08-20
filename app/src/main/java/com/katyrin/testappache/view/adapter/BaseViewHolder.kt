package com.katyrin.testappache.view.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.testappache.model.ContentData

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(dataItem: ContentData)
}