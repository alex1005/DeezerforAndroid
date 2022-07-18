package com.alexjprog.deezerforandroid.ui.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.SearchSuggestionItemBinding
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel

class SearchSuggestionListAdapter(
    val data: List<SearchSuggestionModel>
):
    RecyclerView.Adapter<SearchSuggestionListAdapter.SearchSuggestionViewHolder>() {

    inner class SearchSuggestionViewHolder(private val binding: SearchSuggestionItemBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        fun onBindView(item: SearchSuggestionModel) {
            with(binding) {
                tvSuggestion.text = item.title
                val typeImage = if(item.isInHistory) R.drawable.ic_baseline_history_24
                else R.drawable.ic_baseline_search_24
                ivSuggestionType.setImageResource(typeImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSuggestionViewHolder {
        val binding =
            SearchSuggestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchSuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchSuggestionViewHolder, position: Int) {
        val item = data[position]
        holder.onBindView(item)
    }

    override fun getItemCount(): Int = data.size
}