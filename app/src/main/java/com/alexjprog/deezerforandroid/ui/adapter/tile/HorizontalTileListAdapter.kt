package com.alexjprog.deezerforandroid.ui.adapter.tile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.TileItemLinearBinding
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.util.ImageHelper

class HorizontalTileListAdapter(
    private val data: List<MediaItemModel>,
    private val openPlayerAction: (MediaItemModel) -> Unit
) :
    RecyclerView.Adapter<HorizontalTileListAdapter.LinearTileViewHolder>() {

    inner class LinearTileViewHolder(private val binding: TileItemLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(item: MediaItemModel) {
            with(binding) {
                tvTitle.text = item.title
                tvSubtitle.text = item.subtitle
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
                    }
                }
                root.setOnClickListener { openPlayerAction(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinearTileViewHolder {
        val binding = TileItemLinearBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return LinearTileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinearTileViewHolder, position: Int) {
        val item = data[position]
        holder.onBindView(item)
    }

    override fun getItemCount(): Int = data.size

}