package com.alexjprog.deezerforandroid.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.databinding.ActivityMainBinding
import com.alexjprog.deezerforandroid.service.DeezerFirebaseMessagingService
import com.alexjprog.deezerforandroid.ui.mvp.LoginActivity
import com.alexjprog.deezerforandroid.util.FIREBASE_MEDIA_ID_KEY
import com.alexjprog.deezerforandroid.util.FIREBASE_MEDIA_TYPE_KEY
import com.alexjprog.deezerforandroid.util.putPlayerArgs

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.mainToolbar)

        navController =
            (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
                .navController

        binding.bottomNavigationView.also {
            navController.addOnDestinationChangedListener { controller, destination, _ ->
                if (destination.id != it.selectedItemId) {
                    controller.backQueue.asReversed().drop(1).forEach { entry ->
                        it.menu.forEach { item ->
                            if (entry.destination.id == item.itemId) {
                                item.isChecked = true
                                return@addOnDestinationChangedListener
                            }
                        }
                    }
                }
            }
        }.setupWithNavController(navController)

        openPlayerIfHasExtras()
    }

    private fun openPlayerIfHasExtras() {
        val mediaId = DeezerFirebaseMessagingService.extractMediaIdArg(
            intent.extras?.getString(
                FIREBASE_MEDIA_ID_KEY
            )
        ) ?: return
        val mediaType = DeezerFirebaseMessagingService.extractMediaTypeArg(
            intent.extras?.getString(
                FIREBASE_MEDIA_TYPE_KEY
            )
        )
        navController.navigate(
            R.id.player_nav_graph,
            Bundle().putPlayerArgs(mediaId, mediaType)
        )
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.homeFragment) finishAfterTransition()
        else {
            with(binding.bottomNavigationView) {
                val previousItem = menu.findItem(
                    navController.previousBackStackEntry?.destination?.id ?: 0
                )
                val currentItem = menu.findItem(
                    navController.currentDestination?.id ?: 0
                )
                if (previousItem != null && currentItem != null) {
                    NavigationUI.onNavDestinationSelected(
                        previousItem,
                        navController
                    )
                } else super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_menu -> {
                navController.navigate(R.id.search_nav_graph)
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
        showActionBar()
        showBottomNavigation()
    }

    fun hideAllNavigation() {
        hideActionBar()
        hideBottomNavigation()
    }

    private fun showBottomNavigation() {
        binding.bottomNavigationView.apply {
            animate()
                .translationY(0.0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {}
                })
        }
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigationView.apply {
            animate()
                .translationY(height.toFloat())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        visibility = View.GONE
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        visibility = View.GONE
                    }

                    override fun onAnimationRepeat(animation: Animator?) {}
                })
        }
    }

    fun showActionBar() {
        supportActionBar?.show()
    }

    fun hideActionBar() {
        supportActionBar?.hide()
    }
}