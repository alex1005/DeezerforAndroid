package com.alexjprog.deezerforandroid.ui.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentSearchResultsBinding
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.MediaTypeParam
import com.alexjprog.deezerforandroid.ui.MainActivity
import com.alexjprog.deezerforandroid.ui.adapter.tile.MediaItemComparator
import com.alexjprog.deezerforandroid.ui.adapter.tile.TileFlowAdapter
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
            by navGraphViewModels(R.id.navGraph) { viewModelFactory }

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

    private val openPlayerAction: (MediaItemModel) -> Unit = { mediaItem ->
        findNavController().navigate(
            SearchResultsFragmentDirections.actionOpenPlayerFragmentFromSearchResults(
                mediaItem.id,
                when (mediaItem) {
                    is AlbumModel -> MediaTypeParam.ALBUM
                    is TrackModel -> MediaTypeParam.TRACK
                }
            )
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
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).hideAllNavigation()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as MainActivity).showAllNavigation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}