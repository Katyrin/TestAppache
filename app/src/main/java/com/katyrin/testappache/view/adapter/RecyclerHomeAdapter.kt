package com.katyrin.testappache.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.testappache.databinding.ItemNewBinding
import com.katyrin.testappache.databinding.ItemSavedBinding
import com.katyrin.testappache.model.ContentData

class RecyclerHomeAdapter(
    private var onClick: (ContentData) -> Unit
) : RecyclerView.Adapter<BaseViewHolder>() {

    private var data: List<ContentData> = listOf()

    fun updateData(newData: List<ContentData>) {
        data = newData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        with(LayoutInflater.from(parent.context)) {
            when (viewType) {
                TYPE_NEW -> NewViewHolder(ItemNewBinding.inflate(this, parent, false))
                else -> SavedViewHolder(ItemSavedBinding.inflate(this, parent, false))
            }
        }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int =
        when (position) {
            FIRST_POSITION -> TYPE_NEW
            else -> TYPE_SAVED
        }

    private inner class NewViewHolder(
        private val itemBinding: ItemNewBinding
    ) : BaseViewHolder(itemBinding.root) {
        override fun bind(dataItem: ContentData) {
            itemBinding.root.setOnClickListener { onClick(dataItem) }
        }
    }

    private inner class SavedViewHolder(
        private val itemBinding: ItemSavedBinding
    ) : BaseViewHolder(itemBinding.root) {
        override fun bind(dataItem: ContentData) {
            itemBinding.root.setOnClickListener { onClick(dataItem) }
        }
    }

    private companion object {
        const val FIRST_POSITION = 0
        const val TYPE_NEW = 0
        const val TYPE_SAVED = 1
    }
}