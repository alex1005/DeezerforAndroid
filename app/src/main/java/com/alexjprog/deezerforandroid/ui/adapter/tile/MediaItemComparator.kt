package com.alexjprog.deezerforandroid.ui.adapter.tile

import androidx.recyclerview.widget.DiffUtil
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel

object MediaItemComparator : DiffUtil.ItemCallback<MediaItemModel>() {
    override fun areItemsTheSame(oldItem: MediaItemModel, newItem: MediaItemModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MediaItemModel, newItem: MediaItemModel): Boolean {
        return oldItem == newItem
    }
}