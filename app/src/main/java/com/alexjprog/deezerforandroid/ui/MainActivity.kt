package com.alexjprog.deezerforandroid.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.ActivityMainBinding
import com.alexjprog.deezerforandroid.ui.mvp.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
                .navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.search_menu -> {
                findNavController(R.id.navHostFragment).navigate(R.id.searchFragment)
                true
            }
            R.id.account_menu -> {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showAllNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
        supportActionBar?.show()
    }

    fun hideAllNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
        supportActionBar?.hide()
    }
}