package com.alexjprog.deezerforandroid.ui.adapter.tile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.TileItemLinearBinding
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.util.ImageHelper

class HorizontalTileListAdapter(private val data: List<TrackModel>):
    RecyclerView.Adapter<HorizontalTileListAdapter.LinearTileViewHolder>() {

    class LinearTileViewHolder(private val binding: TileItemLinearBinding) :
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