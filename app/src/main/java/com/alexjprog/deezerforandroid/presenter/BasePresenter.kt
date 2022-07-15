package com.alexjprog.deezerforandroid.presenter

import com.alexjprog.deezerforandroid.ui.mvp.BaseView

abstract class BasePresenter<V: BaseView>: MVPPresenter<V> {
    final override var view: V? = null
        private set

    override fun onAttach(view: V) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }
}