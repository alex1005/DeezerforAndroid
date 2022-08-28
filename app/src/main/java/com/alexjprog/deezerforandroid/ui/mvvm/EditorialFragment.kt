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
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListAdapter
import com.alexjprog.deezerforandroid.ui.base.LoadableFragment
import com.alexjprog.deezerforandroid.util.OpenMoreContentFragmentAction
import com.alexjprog.deezerforandroid.util.OpenPlayerFragmentAction
import com.alexjprog.deezerforandroid.util.getSafeArgPlayerNavDirection
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

    private val openMoreAction: OpenMoreContentFragmentAction = { category ->
        findNavController()
            .navigate(EditorialFragmentDirections.actionOpenMoreContentFromEditorial(category))
    }

    private val openPlayerAction: OpenPlayerFragmentAction = { mediaItem ->
        findNavController().navigate(
            mediaItem.getSafeArgPlayerNavDirection { id, mediaType ->
                EditorialFragmentDirections.actionOpenPlayerFragmentFromEditorial(id, mediaType)
            }
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
        binding.swipeRefresh.setDefaultColorPalette()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            with(viewModel) {
                feed.observe(viewLifecycleOwner) {
                    if (it != null) {
                        rcEditorialFeed.adapter =
                            ComplexListAdapter(it, openMoreAction, openPlayerAction)
                        rcEditorialFeed.visibility = View.VISIBLE
                    } else rcEditorialFeed.visibility = View.GONE
                }

                isLoading.observe(viewLifecycleOwner) {
                    if (it != true) {
                        swipeRefresh.isRefreshing = false
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