package com.alexjprog.deezerforandroid.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.ActivityMainBinding
import com.alexjprog.deezerforandroid.ui.mvp.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
                .navController
        with(binding) {
            bottomNavigationView.setupWithNavController(navController)
            bottomNavigationView.setOnItemSelectedListener { menuItem ->
                if (menuItem.itemId !in setOf(R.id.homeFragment, R.id.editorialFragment)) false
                else {
                    if (!navController.popBackStack(menuItem.itemId, false)) {
                        navController.navigate(menuItem.itemId)
                    }
                    true
                }
            }
            bottomNavigationView.setOnItemReselectedListener { /* Do nothing */ }
        }
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) finishAfterTransition()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_menu -> {
                findNavController(R.id.navHostFragment).navigate(R.id.search_nav_graph)
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