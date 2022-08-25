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
import com.alexjprog.deezerforandroid.util.OpenPlayerFragmentAction

class HorizontalTileListAdapter(
    private val data: List<MediaItemModel>,
    private val openPlayerAction: OpenPlayerFragmentAction
) :
    RecyclerView.Adapter<HorizontalTileListAdapter.HorizontalTileViewHolder>() {

    inner class HorizontalTileViewHolder(private val binding: TileItemLinearBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(item: MediaItemModel) {
            with(binding) {
                tvTitle.text = item.title
                tvSubtitle.text = item.subtitle
                ivCover.also {
                    when (item) {
                        is TrackModel ->
                            ImageHelper.loadRoundedCornersCover(
                                it,
                                item.pictureLink,
                                it.resources.getDimensionPixelSize(R.dimen.tile_corner_radius)
                            )
                        is AlbumModel -> {
                            it.foreground =
                                ContextCompat.getDrawable(it.context, R.drawable.tile_ripple_round)
                            ImageHelper.loadRoundCover(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalTileViewHolder {
        val binding = TileItemLinearBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return HorizontalTileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HorizontalTileViewHolder, position: Int) {
        val item = data[position]
        holder.onBindView(item)
    }

    override fun getItemCount(): Int = data.size

}