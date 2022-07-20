package com.alexjprog.deezerforandroid.ui.adapter.tile

import androidx.recyclerview.widget.DiffUtil
import com.alexjprog.deezerforandroid.domain.model.TrackModel

object TrackModelComparator : DiffUtil.ItemCallback<TrackModel>() {
    override fun areItemsTheSame(oldItem: TrackModel, newItem: TrackModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrackModel, newItem: TrackModel): Boolean {
        return oldItem == newItem
    }
}