package com.alexjprog.deezerforandroid.ui.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentHomeBinding
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.model.MediaTypeParam
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListAdapter
import com.alexjprog.deezerforandroid.ui.base.LoadableFragment
import com.alexjprog.deezerforandroid.viewmodel.HomeViewModel
import com.alexjprog.deezerforandroid.viewmodel.ViewModelFactory
import javax.inject.Inject

class HomeFragment : LoadableFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    override val viewModel: HomeViewModel
            by viewModels(factoryProducer = { viewModelFactory })

    private val openMoreAction: (ContentCategory) -> Unit = { category ->
        findNavController().navigate(HomeFragmentDirections.actionOpenMoreContentFromHome(category))
    }

    private val openPlayerAction: (MediaItemModel) -> Unit = { mediaItem ->
        findNavController().navigate(
            HomeFragmentDirections.actionOpenPlayerFragmentFromHome(
                mediaItem.id,
                when (mediaItem) {
                    is AlbumModel -> MediaTypeParam.ALBUM
                    is TrackModel -> MediaTypeParam.TRACK
                }
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        with(viewModel) {
            feed.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.rcHomeFeed.adapter =
                        ComplexListAdapter(it, openMoreAction, openPlayerAction)
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTryAgain() {
        viewModel.loadFeed()
    }
}