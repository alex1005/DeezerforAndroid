package com.alexjprog.deezerforandroid.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.viewmodel.LoadableViewModel

abstract class LoadableFragment : Fragment(), LoadingFragment.LoadingFragmentListener {
    protected abstract val viewModel: LoadableViewModel
    private var loadingFragment: LoadingFragment? = null

    private fun startLoading() {
        if (loadingFragment == null) {
            loadingFragment = LoadingFragment().also {
                childFragmentManager.beginTransaction()
                    .add(R.id.loadingHost, it)
                    .commitNow()
            }
        }
        loadingFragment?.startLoading()
    }

    private fun onLoadSuccessful() {
        loadingFragment?.let {
            childFragmentManager.beginTransaction()
                .remove(it)
                .commitNow()
        }
        loadingFragment = null
    }

    private fun onLoadFail() {
        loadingFragment?.showError()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoading.observe(viewLifecycleOwner) {
            when (it) {
                true -> startLoading()
                false -> onLoadSuccessful()
                null -> onLoadFail()
            }
        }
    }
}