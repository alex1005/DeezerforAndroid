package com.alexjprog.deezerforandroid.ui.adapter.complex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.HorizontalTrackListItemBinding
import com.alexjprog.deezerforandroid.databinding.TitleItemBinding
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.tile.HorizontalTileListAdapter

class ComplexListAdapter(
    private val data: List<ComplexListItem>,
    private val openMoreAction: (ContentCategory) -> Unit
) :
    RecyclerView.Adapter<ComplexViewHolder>() {

    inner class TitleViewHolder(private val binding: TitleItemBinding) :
        ComplexViewHolder(binding.root) {
        fun onBindView(item: ComplexListItem.TitleItem) {
            with(binding) {
                tvTitle.also {
                    it.text = it.resources.getString(item.category.titleResId)
                }
                root.setOnClickListener { openMoreAction(item.category) }
                btnMoreContent.setOnClickListener { openMoreAction(item.category) }
            }
        }
    }

    inner class HorizontalTrackListViewHolder(private val binding: HorizontalTrackListItemBinding) :
        ComplexViewHolder(binding.root) {
        fun onBindView(item: ComplexListItem.HorizontalTrackListItem) {
            with(binding) {
                rcHorizontalList.adapter = HorizontalTileListAdapter(item.data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (data[position]) {
        is ComplexListItem.TitleItem -> R.layout.title_item
        is ComplexListItem.HorizontalTrackListItem -> R.layout.horizontal_track_list_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplexViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.title_item ->
                TitleViewHolder(TitleItemBinding.inflate(layoutInflater, parent, false))
            R.layout.horizontal_track_list_item ->
                HorizontalTrackListViewHolder(
                    HorizontalTrackListItemBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            else -> throw IllegalArgumentException("no such complex item")
        }
    }

    override fun onBindViewHolder(holder: ComplexViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is TitleViewHolder -> holder.onBindView(item as ComplexListItem.TitleItem)
            is HorizontalTrackListViewHolder ->
                holder.onBindView(item as ComplexListItem.HorizontalTrackListItem)
        }
    }

    override fun getItemCount(): Int = data.size
}