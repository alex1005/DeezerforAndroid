package com.alexjprog.deezerforandroid.ui.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentMoreContentBinding
import com.alexjprog.deezerforandroid.ui.adapter.tile.MediaItemComparator
import com.alexjprog.deezerforandroid.ui.adapter.tile.TileFlowAdapter
import com.alexjprog.deezerforandroid.util.OpenPlayerFragmentAction
import com.alexjprog.deezerforandroid.util.getSafeArgPlayerNavDirection
import com.alexjprog.deezerforandroid.viewmodel.MoreContentViewModel
import com.alexjprog.deezerforandroid.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoreContentFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentMoreContentBinding? = null
    private val binding: FragmentMoreContentBinding get() = _binding!!
    private val viewModel: MoreContentViewModel
            by navGraphViewModels(R.id.navGraph) { viewModelFactory }

    private val args: MoreContentFragmentArgs by navArgs()

    private val openPlayerAction: OpenPlayerFragmentAction = { mediaItem ->
        findNavController().navigate(
            mediaItem.getSafeArgPlayerNavDirection { id, mediaType ->
                MoreContentFragmentDirections.actionOpenPlayerFragmentFromMore(id, mediaType)
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as DeezerApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {

            if (contentFlow == null) loadCategory(args.category)

            with(binding) {
                val contentAdapter = TileFlowAdapter(MediaItemComparator, openPlayerAction)
                rcContent.adapter = contentAdapter
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    contentFlow?.collectLatest { pagingData ->
                        contentAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}