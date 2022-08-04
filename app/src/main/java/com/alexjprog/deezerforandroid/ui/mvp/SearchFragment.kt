package com.alexjprog.deezerforandroid.ui.mvp

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentSearchBinding
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.ui.MainActivity
import com.alexjprog.deezerforandroid.ui.adapter.search.SearchSuggestionListAdapter
import com.alexjprog.deezerforandroid.ui.mvp.contract.SearchContract
import com.alexjprog.deezerforandroid.util.SEARCH_SUGGESTIONS_LIST_STATE_KEY
import com.alexjprog.deezerforandroid.util.SaveStateHelper
import javax.inject.Inject

class SearchFragment : Fragment(), SearchContract.View {

    @Inject
    lateinit var presenter: SearchContract.Presenter

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!

    private var searchSuggestionListState: Parcelable? = null

    private val backAction: (View) -> Unit = {
        findNavController().navigateUp()
    }

    private val clearAction: (View) -> Unit = {
        binding.inputField.etSearch.text.clear()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as DeezerApplication).appComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        presenter.subscribeToSearchInput()
        if (savedInstanceState == null)
            presenter.postSearchQuery("")

        with(binding) {
            with(inputField) {
                btnBack.setOnClickListener(backAction)
                btnClear.setOnClickListener(clearAction)
                etSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        btnClear.visibility = if (s == null || s.isEmpty()) View.GONE
                        else View.VISIBLE
                        if (etSearch.hasFocus())
                            s?.let { presenter.postSearchQuery(it.toString()) }
                    }
                })
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).hideAllNavigation()
        binding.inputField.etSearch.requestFocus()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            with(binding) {
                val restoredData =
                    SaveStateHelper.restoreSearchSuggestionModelList(savedInstanceState)
                rcSearchSuggestions.adapter = SearchSuggestionListAdapter(restoredData)
                SaveStateHelper.restoreRecyclerViewState(
                    savedInstanceState,
                    SEARCH_SUGGESTIONS_LIST_STATE_KEY,
                    rcSearchSuggestions
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(binding) {
            val data = (rcSearchSuggestions.adapter as? SearchSuggestionListAdapter)?.data
            SaveStateHelper.saveSearchSuggestionModelList(outState, data)
            SaveStateHelper.saveRecyclerViewState(
                outState,
                SEARCH_SUGGESTIONS_LIST_STATE_KEY,
                searchSuggestionListState
            )
        }
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        searchSuggestionListState =
            binding.rcSearchSuggestions.layoutManager?.onSaveInstanceState()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).showAllNavigation()
        binding.inputField.etSearch.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        presenter.onDetach()
    }

    override fun updateSearchSuggestions(data: List<SearchSuggestionModel>) {
        binding.rcSearchSuggestions.adapter = SearchSuggestionListAdapter(data)
    }

}