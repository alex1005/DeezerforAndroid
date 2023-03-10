package com.alexjprog.deezerforandroid.ui.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentSearchResultsBinding
import com.alexjprog.deezerforandroid.ui.MainActivity
import com.alexjprog.deezerforandroid.ui.adapter.tile.MediaItemComparator
import com.alexjprog.deezerforandroid.ui.adapter.tile.TileFlowAdapter
import com.alexjprog.deezerforandroid.util.OpenPlayerFragmentAction
import com.alexjprog.deezerforandroid.util.getSafeArgPlayerNavDirection
import com.alexjprog.deezerforandroid.viewmodel.SearchResultsViewModel
import com.alexjprog.deezerforandroid.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchResultsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding: FragmentSearchResultsBinding get() = _binding!!
    private val viewModel: SearchResultsViewModel
            by viewModels { viewModelFactory }

    private val args: SearchResultsFragmentArgs by navArgs()

    private val backAction: View.OnClickListener = View.OnClickListener {
        findNavController().navigateUp()
    }

    private val clearAction: View.OnClickListener = View.OnClickListener {
        openNewSearch(null)
    }

    private val newSearchAction: View.OnClickListener = View.OnClickListener {
        openNewSearch((it as? TextView)?.text.toString())
    }

    private val openPlayerAction: OpenPlayerFragmentAction = { mediaItem ->
        findNavController().navigate(
            mediaItem.getSafeArgPlayerNavDirection { id, mediaType ->
                SearchResultsFragmentDirections.actionOpenPlayerFragmentFromSearchResults(
                    id,
                    mediaType
                )
            }
        )
    }

    private fun openNewSearch(oldQuery: String?) {
        findNavController().navigate(
            SearchResultsFragmentDirections.actionOpenSearchFragmentFromSearchResults(
                oldQuery
            )
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as DeezerApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(inputField) {
                btnBack.setOnClickListener(backAction)

                btnClear.setOnClickListener(clearAction)
                btnClear.visibility = View.VISIBLE

                etSearch.setOnClickListener(newSearchAction)
                etSearch.setText(args.query)
                etSearch.isFocusable = false
            }

            with(viewModel) {
                if (resultsFlow == null) loadResults(args.query)

                val contentAdapter = TileFlowAdapter(MediaItemComparator, openPlayerAction)
                rcContent.adapter = contentAdapter
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    resultsFlow?.collectLatest { pagingData ->
                        contentAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).hideActionBar()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).showActionBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}