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
import com.alexjprog.deezerforandroid.databinding.FragmentEditorialBinding
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.MediaTypeParam
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListAdapter
import com.alexjprog.deezerforandroid.ui.base.LoadableFragment
import com.alexjprog.deezerforandroid.viewmodel.EditorialViewModel
import com.alexjprog.deezerforandroid.viewmodel.ViewModelFactory
import javax.inject.Inject

class EditorialFragment : LoadableFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentEditorialBinding? = null
    private val binding: FragmentEditorialBinding get() = _binding!!
    override val viewModel: EditorialViewModel
            by navGraphViewModels(R.id.navGraph) { viewModelFactory }

    private val openMoreAction: (ContentCategory) -> Unit = { category ->
        findNavController()
            .navigate(EditorialFragmentDirections.actionOpenMoreContentFromEditorial(category))
    }

    private val openPlayerAction: (MediaItemModel) -> Unit = { mediaItem ->
        findNavController().navigate(
            EditorialFragmentDirections.actionOpenPlayerFragmentFromEditorial(
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
        _binding = FragmentEditorialBinding.inflate(inflater, container, false)

        viewModel.feed.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.rcHomeFeed.adapter =
                    ComplexListAdapter(it, openMoreAction, openPlayerAction)
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