package com.alexjprog.deezerforandroid.ui.adapter.tile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.TileItemGridBinding
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.util.ImageHelper

class TileFlowAdapter(diffCallback: DiffUtil.ItemCallback<MediaItemModel>) :
    PagingDataAdapter<MediaItemModel, TileFlowAdapter.GridTileViewHolder>(diffCallback) {

    class GridTileViewHolder(private val binding: TileItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(item: MediaItemModel?) {
            with(binding) {
                tvTitle.text = item?.title ?: root.resources.getString(R.string.unknown)
                tvSubtitle.text = item?.subtitle ?: root.resources.getString(R.string.unknown)
                ivCover.also {
                    when (item) {
                        is TrackModel ->
                            ImageHelper.loadRoundedCornersPicture(
                                it,
                                item.pictureLink,
                                it.resources.getDimensionPixelSize(R.dimen.tile_corner_radius)
                            )
                        is AlbumModel -> {
                            it.foreground =
                                ContextCompat.getDrawable(it.context, R.drawable.tile_ripple_round)
                            ImageHelper.loadRoundPicture(
                                it,
                                item.pictureLink
                            )
                        }
                        else -> {}
                    }
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