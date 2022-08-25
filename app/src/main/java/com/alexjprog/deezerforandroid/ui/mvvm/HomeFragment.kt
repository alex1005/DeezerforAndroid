package com.alexjprog.deezerforandroid.ui.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentHomeBinding
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.MediaTypeParam
import com.alexjprog.deezerforandroid.model.ContentCategory
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
            by navGraphViewModels(R.id.navGraph) { viewModelFactory }

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewModel) {
            with(binding) {
                feed.observe(viewLifecycleOwner) {
                    if (it != null) {
                        rcHomeFeed.adapter =
                            ComplexListAdapter(it, openMoreAction, openPlayerAction)
                    }
                    swipeRefresh.isRefreshing = false
                }

                isLoading.observe(viewLifecycleOwner) {
                    if (it != true) {
                        rcHomeFeed.visibility = View.VISIBLE
                        swipeRefresh.isRefreshing = false
                    } else {
                        rcHomeFeed.visibility = View.GONE
                    }
                }

                swipeRefresh.setOnRefreshListener {
                    loadFeed()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTryAgain() {
        viewModel.loadFeed()
    }
}