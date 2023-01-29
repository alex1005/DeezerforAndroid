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
import androidx.navigation.fragment.navArgs
import com.alexjprog.deezerforandroid.BuildConfig
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.FragmentLoginBinding
import com.alexjprog.deezerforandroid.ui.mvp.contract.LoginContract
import javax.inject.Inject

class LoginFragment : Fragment(), LoginContract.View {
    @Inject
    lateinit var presenter: LoginContract.Presenter

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    private val args: LoginFragmentArgs by navArgs()

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
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener { openAppropriateLoginApp() }

        val token = args.token
        presenter.checkAndSaveUserToken(token)
    }

    private fun openAppropriateLoginApp() {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(LOGIN_URL)
        }
        startActivity(intent)
    }

    override fun showLoginButton() {
        binding.btnLogin.visibility = View.VISIBLE
    }

    override fun onSuccessfulLogin() {
        findNavController().navigate(LoginFragmentDirections.actionOpenUserInfoFragmentFromLogin())
    }

    override fun showErrorMessage() {
        Toast.makeText(context, R.string.login_error, Toast.LENGTH_LONG).show()
    }

    override fun showLoading() {
        binding.waveLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.waveLoading.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDetach()
        _binding = null
    }

    companion object {
        private const val LOGIN_URL =
            "https://connect.deezer.com/oauth/auth.php?app_id=${BuildConfig.API_ACCESS_TOKEN}&" +
                    "redirect_uri=${BuildConfig.API_REDIRECT_URI}&" +
                    "perms=basic_access,email,listening_history,offline_access&response_type=token"
    }
}