package com.alexjprog.deezerforandroid.ui.adapter.complex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.HorizontalTrackListItemBinding
import com.alexjprog.deezerforandroid.databinding.TitleItemBinding
import com.alexjprog.deezerforandroid.databinding.TitleItemWithMoreActionBinding
import com.alexjprog.deezerforandroid.ui.adapter.tile.HorizontalTileListAdapter
import com.alexjprog.deezerforandroid.util.OpenMoreContentFragmentAction
import com.alexjprog.deezerforandroid.util.OpenPlayerFragmentAction

class ComplexListAdapter(
    private val data: List<ComplexListItem>,
    private val openMoreAction: OpenMoreContentFragmentAction,
    private val openPlayerAction: OpenPlayerFragmentAction
) :
    RecyclerView.Adapter<ComplexViewHolder>() {

    inner class TitleWithMoreActionViewHolder(private val binding: TitleItemWithMoreActionBinding) :
        ComplexViewHolder(binding.root) {
        fun onBindView(item: ComplexListItem.TitleItemWithOpenMoreAction) {
            with(binding) {
                tvTitle.also {
                    it.text = it.resources.getString(item.category.titleResId)
                }
                root.setOnClickListener { openMoreAction(item.category) }
                btnMoreContent.setOnClickListener { openMoreAction(item.category) }
            }
        }
    }

    inner class TitleViewHolder(private val binding: TitleItemBinding) :
        ComplexViewHolder(binding.root) {
        fun onBindView(item: ComplexListItem.TitleItem) {
            with(binding) {
                tvTitle.also {
                    it.text = it.resources.getString(item.category.titleResId)
                }
            }
        }
    }

    inner class HorizontalTrackListViewHolder(private val binding: HorizontalTrackListItemBinding) :
        ComplexViewHolder(binding.root) {
        fun onBindView(item: ComplexListItem.HorizontalTrackListItem) {
            with(binding) {
                rcHorizontalList.adapter = HorizontalTileListAdapter(item.data, openPlayerAction)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when (data[position]) {
        is ComplexListItem.TitleItemWithOpenMoreAction -> R.layout.title_item_with_more_action
        is ComplexListItem.TitleItem -> R.layout.title_item
        is ComplexListItem.HorizontalTrackListItem -> R.layout.horizontal_track_list_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplexViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.title_item -> TitleViewHolder(
                TitleItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            R.layout.title_item_with_more_action ->
                TitleWithMoreActionViewHolder(
                    TitleItemWithMoreActionBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
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
            is TitleWithMoreActionViewHolder -> holder.onBindView(item as ComplexListItem.TitleItemWithOpenMoreAction)
            is TitleViewHolder -> holder.onBindView(item as ComplexListItem.TitleItem)
            is HorizontalTrackListViewHolder ->
                holder.onBindView(item as ComplexListItem.HorizontalTrackListItem)
        }
    }

    override fun getItemCount(): Int = data.size
}