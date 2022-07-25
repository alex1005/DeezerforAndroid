package com.alexjprog.deezerforandroid.ui.mvvm

import android.content.Context
import android.os.Bundle
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
            by activityViewModels { viewModelFactory }

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

            if (contentFlow == null) loadCategory(args.category)

            with(binding) {
                val contentAdapter = TileFlowAdapter(TrackModelComparator)
                rcContent.adapter = contentAdapter
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                    contentFlow?.collectLatest { pagingData ->
                        contentAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}