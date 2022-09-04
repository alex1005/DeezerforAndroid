package com.alexjprog.deezerforandroid.presenter

import com.alexjprog.deezerforandroid.ui.mvp.BaseView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

abstract class BasePresenter<V: BaseView>: MVPPresenter<V> {
    final override var view: V? = null
        private set
    protected val disposableBag = CompositeDisposable()

    override fun onAttach(view: V) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
        disposableBag.dispose()
    }

    protected fun Disposable.addDisposable() {
        disposableBag.add(this)
    }
}