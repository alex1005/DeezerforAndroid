package com.alexjprog.deezerforandroid.presenter

import com.alexjprog.deezerforandroid.ui.mvp.BaseView

interface MVPPresenter<V: BaseView> {
    val view: V?
    fun onAttach(view: V)
    fun onDetach()
}