package com.alexjprog.deezerforandroid.presenter

import com.alexjprog.deezerforandroid.ui.mvp.contract.PlayerContract
import javax.inject.Inject

class PlayerPresenter @Inject constructor(

) : BasePresenter<PlayerContract.View>(), PlayerContract.Presenter {

}
