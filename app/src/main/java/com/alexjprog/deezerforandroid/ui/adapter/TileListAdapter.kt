package com.alexjprog.deezerforandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexjprog.deezerforandroid.databinding.TileItemBinding
import com.alexjprog.deezerforandroid.domain.model.TrackModel

class TileListAdapter(private val data: List<TrackModel>):
    RecyclerView.Adapter<TileListAdapter.TileViewHolder>() {

    inner class TileViewHolder(private val binding: TileItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(track: TrackModel) {
            with(binding) {
                tvTitle.text = track.title
                tvArtist.text = track.artist.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val binding = TileItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        val item = data[position]
        holder.onBindView(item)
    }

    override fun getItemCount(): Int = data.size
}