package com.alexjprog.deezerforandroid.ui.mvp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.databinding.ActivityLoginBinding
import com.alexjprog.deezerforandroid.presenter.LoginPresenter
import com.alexjprog.deezerforandroid.ui.MainActivity
import com.alexjprog.deezerforandroid.ui.mvp.contract.LoginContract
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginContract.View {
    @Inject
    lateinit var presenter: LoginPresenter

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (application as DeezerApplication).appComponent.inject(this)

        with(presenter) {
            onAttach(this@LoginActivity)
            val authUri = intent.data
            if(authUri != null) {
                extractAndSaveUserToken(authUri)
            } else {
                checkLoginState()
            }
        }

        binding.btnLogin.setOnClickListener { openAppropriateLoginApp() }
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
        val intent = Intent(this, MainActivity::class.java).apply {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    companion object {
        private const val APP_ID = ***REMOVED***
        private const val REDIRECT_URI = ***REMOVED***
        private const val LOGIN_URL = "https://connect.deezer.com/oauth/auth.php?app_id=$APP_ID&" +
                "redirect_uri=$REDIRECT_URI&perms=basic_access,email,listening_history,offline_access&response_type=token"
    }
}