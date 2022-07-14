package com.alexjprog.deezerforandroid.ui.adapter.tile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.TileItemGridBinding
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.util.ImageHelper

class VerticalTileListAdapter(private val data: List<TrackModel>):
    RecyclerView.Adapter<VerticalTileListAdapter.GridTileViewHolder>() {

    class GridTileViewHolder(private val binding: TileItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(track: TrackModel) {
            with(binding) {
                tvTitle.text = track.title
                tvArtist.text = track.artist.name
                ivCover.also {
                    ImageHelper.loadRoundedPicture(
                        it,
                        track.album.cover,
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
        val item = data[position]
        holder.onBindView(item)
    }

    override fun getItemCount(): Int = data.size

}