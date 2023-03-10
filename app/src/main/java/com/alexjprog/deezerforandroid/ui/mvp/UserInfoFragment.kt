package com.alexjprog.deezerforandroid.ui.mvp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentUserInfoBinding
import com.alexjprog.deezerforandroid.model.UserInfoDisplayable
import com.alexjprog.deezerforandroid.ui.mvp.contract.UserInfoContract
import com.alexjprog.deezerforandroid.util.ImageHelper
import javax.inject.Inject

class UserInfoFragment : Fragment(), UserInfoContract.View {
    @Inject
    lateinit var presenter: UserInfoContract.Presenter

    private var _binding: FragmentUserInfoBinding? = null
    private val binding: FragmentUserInfoBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as DeezerApplication).appComponent.inject(this)
        presenter.onAttach(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadUserInfo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
        _binding = null
    }

    override fun showUserInfo(userInfo: UserInfoDisplayable) {
        with(binding) {
            with(userInfo) {
                ImageHelper.loadUserIconIntoImage(ivAccountPicture, bigPictureLink)
                fabViewInWeb.setOnClickListener {
                    openUserInfoInWeb(linkToDeezer)
                }

                tvName.text = resources.getString(
                    R.string.user_name,
                    firstname,
                    lastname
                )
                tvEmail.text = email

                tvBirthday.text = birthday ?: resources.getString(R.string.unknown)
                tvInspirationDate.text = inscriptionDate

                tvCountry.text = country
            }
        }
    }

    override fun showErrorAndClose() {
        Toast.makeText(context, R.string.user_info_fetch_error, Toast.LENGTH_LONG).show()
        findNavController().navigateUp()
    }

    private fun openUserInfoInWeb(uri: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(uri)
        }
        startActivity(intent)
    }
}