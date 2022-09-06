package com.alexjprog.deezerforandroid.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alexjprog.deezerforandroid.databinding.FragmentLoadingBinding

class LoadingFragment : Fragment() {

    private var _binding: FragmentLoadingBinding? = null
    private val binding: FragmentLoadingBinding
        get() = _binding!!

    private lateinit var listener: LoadingFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = parentFragment as LoadingFragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)

        with(binding) {
            btnTryAgain.setOnClickListener {
                listener.onTryAgain()
                startLoading()
            }
        }

        return binding.root
    }

    fun startLoading() {
        with(binding) {
            btnTryAgain.visibility = View.GONE
            tvErrorMessage.visibility = View.GONE
            ivLoad.visibility = View.VISIBLE
        }
    }

    fun showError() {
        with(binding) {
            btnTryAgain.visibility = View.VISIBLE
            tvErrorMessage.visibility = View.VISIBLE
            ivLoad.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface LoadingFragmentListener {
        fun onTryAgain()
    }
}