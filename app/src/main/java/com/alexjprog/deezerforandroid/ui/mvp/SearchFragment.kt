package com.alexjprog.deezerforandroid.ui.mvp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentSearchBinding
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.ui.MainActivity
import com.alexjprog.deezerforandroid.ui.adapter.search.SearchSuggestionListAdapter
import com.alexjprog.deezerforandroid.ui.mvp.contract.SearchContract
import javax.inject.Inject

class SearchFragment : Fragment(), SearchContract.View {

    @Inject
    lateinit var presenter: SearchContract.Presenter

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!

    private val args: SearchFragmentArgs by navArgs()

    private val backAction: View.OnClickListener = View.OnClickListener {
        findNavController().navigateUp()
    }

    private val clearAction: View.OnClickListener = View.OnClickListener {
        binding.inputField.etSearch.text.clear()
    }

    private val openResultsAction: (String) -> Unit = { query ->
        presenter.saveQueryToHistory(query)
        findNavController().navigate(
            SearchFragmentDirections.actionOpenSearchResultsFragmentFromSearch(
                query
            )
        )
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

                etSearch.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        openResultsAction(etSearch.text.toString())
                    }
                    false
                }
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
        binding.inputField.etSearch.apply {
            requestFocus()
            setText(args.oldQuery ?: "")
        }
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
        binding.rcSearchSuggestions.adapter = SearchSuggestionListAdapter(data, openResultsAction)
    }

}