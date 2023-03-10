package com.alexjprog.deezerforandroid.ui.base

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.viewmodel.LoadableViewModel

abstract class LoadableFragment : Fragment(), LoadingFragment.LoadingFragmentListener {
    protected abstract val viewModel: LoadableViewModel
    private val loadingFragment: LoadingFragment?
        get() = childFragmentManager.findFragmentById(R.id.loadingHost) as? LoadingFragment

    private fun checkAndCreateLoadingFragment() {
        if (loadingFragment == null) {
            LoadingFragment().also {
                childFragmentManager.beginTransaction()
                    .add(R.id.loadingHost, it)
                    .commitNow()
            }
        }
    }

    private fun startLoading() {
        checkAndCreateLoadingFragment()
        loadingFragment?.startLoading()
    }

    private fun onLoadSuccessful() {
        loadingFragment?.let {
            childFragmentManager.beginTransaction()
                .remove(it)
                .commitNow()
        }
    }

    private fun onLoadFail() {
        checkAndCreateLoadingFragment()
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

    protected fun SwipeRefreshLayout.setDefaultColorPalette() {
        setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                context,
                R.color.color_primary_variant
            )
        )
        setColorSchemeColors(
            ContextCompat.getColor(context, R.color.color_secondary),
            ContextCompat.getColor(context, R.color.color_text_primary),
        )
    }
}