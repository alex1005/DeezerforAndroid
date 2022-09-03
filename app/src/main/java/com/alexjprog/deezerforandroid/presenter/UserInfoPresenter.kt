package com.alexjprog.deezerforandroid.presenter

import android.util.Log
import com.alexjprog.deezerforandroid.domain.model.UserInfoModel
import com.alexjprog.deezerforandroid.domain.usecase.GetUserInfoUseCase
import com.alexjprog.deezerforandroid.ui.mvp.contract.UserInfoContract
import com.alexjprog.deezerforandroid.util.MIN_KNOWN_BIRTHDAY
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class UserInfoPresenter @Inject constructor(
    private val getUserInfoUserCase: GetUserInfoUseCase
) : BasePresenter<UserInfoContract.View>(),
    UserInfoContract.Presenter {
    override fun loadUserInfo() {
        getUserInfoUserCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext@{
                showUserInfo(it)
            },
                onError@{
                    //TODO: show error
                    Log.d("userTag", it.message.toString())
                })
    }

    private fun showUserInfo(userInfo: UserInfoModel) {
        val locale = Locale("", userInfo.country)
        val countryText = locale.displayCountry

        val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
        val calendar = Calendar.getInstance()
        calendar.time = userInfo.birthday

        val birthdayText =
            if (calendar.get(Calendar.YEAR) >= MIN_KNOWN_BIRTHDAY) dateFormatter.format(userInfo.birthday) else null
        val inscriptionDateText = dateFormatter.format(userInfo.inscriptionDate)

        view?.showUserInfo(
            userInfo.displayableFirstname,
            userInfo.displayableLastname,
            birthdayText,
            inscriptionDateText,
            userInfo.email,
            countryText,
            userInfo.bigPictureLink,
            userInfo.linkToDeezer
        )
    }
}