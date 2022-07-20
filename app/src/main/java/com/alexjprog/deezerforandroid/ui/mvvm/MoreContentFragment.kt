package com.alexjprog.deezerforandroid.ui.mvvm

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentMoreContentBinding
import com.alexjprog.deezerforandroid.ui.adapter.tile.TileFlowAdapter
import com.alexjprog.deezerforandroid.ui.adapter.tile.TrackModelComparator
import com.alexjprog.deezerforandroid.util.CONTENT_LIST_STATE_KEY
import com.alexjprog.deezerforandroid.util.SaveStateHelper
import com.alexjprog.deezerforandroid.viewmodel.MoreContentViewModel
import com.alexjprog.deezerforandroid.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoreContentFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentMoreContentBinding? = null
    private val binding: FragmentMoreContentBinding get() = _binding!!
    private val viewModel: MoreContentViewModel
            by activityViewModels { viewModelFactory }

    private var contentListState: Parcelable? = null

    private val args: MoreContentFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as DeezerApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreContentBinding.inflate(inflater, container, false)
        with(viewModel) {
            with(binding) {
                val contentAdapter = TileFlowAdapter(TrackModelComparator)
                rcContent.adapter = contentAdapter
                viewLifecycleOwner.lifecycleScope.launch {
                    loadCategory(args.category).collectLatest { pagingData ->
                        contentAdapter.submitData(lifecycle, pagingData)
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            SaveStateHelper.restoreRecyclerViewState(
                savedInstanceState,
                CONTENT_LIST_STATE_KEY,
                binding.rcContent
            )
        }
    }

    override fun onStop() {
        super.onStop()
        contentListState = binding.rcContent.layoutManager?.onSaveInstanceState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        SaveStateHelper.saveRecyclerViewState(
            outState,
            CONTENT_LIST_STATE_KEY,
            contentListState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}