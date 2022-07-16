package com.alexjprog.deezerforandroid.presenter

import com.alexjprog.deezerforandroid.ui.mvp.contract.SearchContract
import javax.inject.Inject

class SearchPresenter @Inject constructor(

): BasePresenter<SearchContract.View>(), SearchContract.Presenter {

}