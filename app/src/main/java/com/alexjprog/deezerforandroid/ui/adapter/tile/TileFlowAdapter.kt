package com.alexjprog.deezerforandroid.ui.adapter.tile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.TileItemGridBinding
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.util.ImageHelper

class TileFlowAdapter(diffCallback: DiffUtil.ItemCallback<TrackModel>) :
    PagingDataAdapter<TrackModel, TileFlowAdapter.GridTileViewHolder>(diffCallback) {

    class GridTileViewHolder(private val binding: TileItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(track: TrackModel?) {
            with(binding) {
                tvTitle.text = track?.title ?: root.resources.getString(R.string.unknown)
                tvArtist.text = track?.artist?.name ?: root.resources.getString(R.string.unknown)
                ivCover.also {
                    ImageHelper.loadRoundedPicture(
                        it,
                        track?.album?.coverBig ?: track?.album?.cover,
                        it.resources.getDimensionPixelSize(R.dimen.tile_corner_radius)
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridTileViewHolder {
        val binding = TileItemGridBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return GridTileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridTileViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBindView(item)
    }
}