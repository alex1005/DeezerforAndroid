package com.alexjprog.deezerforandroid.ui.mvp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentSearchBinding
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.presenter.SearchPresenter
import com.alexjprog.deezerforandroid.ui.adapter.search.SearchSuggestionListAdapter
import com.alexjprog.deezerforandroid.ui.mvp.contract.SearchContract
import com.alexjprog.deezerforandroid.util.ui.onStartOrEndDrawableClicked
import javax.inject.Inject

class SearchFragment : Fragment(), SearchContract.View {

    @Inject
    lateinit var presenter: SearchPresenter

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding get() = _binding!!

    private val backAction: (view: EditText) -> Unit = {
        findNavController().navigateUp()
    }

    private val clearAction: (view: EditText) -> Unit = {
        it.text.clear()
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

        with(binding) {
            etSearch.onStartOrEndDrawableClicked(backAction, clearAction)
            etSearch.requestFocus()

            rcSearchSuggestions.adapter = SearchSuggestionListAdapter(listOf(SearchSuggestionModel("Eminem", true), SearchSuggestionModel("Rock", false)))
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        presenter.onDetach()
    }
}