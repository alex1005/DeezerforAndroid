package com.alexjprog.deezerforandroid.ui.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentEditorialBinding
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListAdapter
import com.alexjprog.deezerforandroid.viewmodel.EditorialViewModel
import com.alexjprog.deezerforandroid.viewmodel.ViewModelFactory
import javax.inject.Inject

class EditorialFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _binding: FragmentEditorialBinding? = null
    private val binding: FragmentEditorialBinding get() = _binding!!
    private val viewModel: EditorialViewModel
            by activityViewModels(factoryProducer = { viewModelFactory })

    private val openMoreAction: (ContentCategory) -> Unit = { category ->
        findNavController()
            .navigate(EditorialFragmentDirections.actionOpenMoreContentFromEditorial(category))
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
                binding.rcHomeFeed.adapter = ComplexListAdapter(it, openMoreAction)
            }
        }
        return binding.root
    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        if (savedInstanceState != null) {
//            SaveStateHelper.restoreRecyclerViewState(
//                savedInstanceState,
//                HOME_FEED_LIST_STATE_KEY,
//                binding.rcHomeFeed
//            )
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        homeFeedListState = binding.rcHomeFeed.layoutManager?.onSaveInstanceState()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        SaveStateHelper.saveRecyclerViewState(
//            outState,
//            HOME_FEED_LIST_STATE_KEY,
//            homeFeedListState
//        )
//        super.onSaveInstanceState(outState)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}