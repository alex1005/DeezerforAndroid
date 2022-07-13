package com.alexjprog.deezerforandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.alexjprog.deezerforandroid.databinding.FragmentHomeBinding
import com.alexjprog.deezerforandroid.ui.adapter.ComplexListAdapter
import com.alexjprog.deezerforandroid.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.feed.observe(viewLifecycleOwner) {
            if(it != null) {
                binding.rcHomeFeed.adapter = ComplexListAdapter(it)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}