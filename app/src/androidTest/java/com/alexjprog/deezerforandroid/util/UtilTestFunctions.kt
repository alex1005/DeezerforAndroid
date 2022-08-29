package com.alexjprog.deezerforandroid.util

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.alexjprog.deezerforandroid.R

fun ActivityScenarioRule<*>.getNavController(): NavController {
    var navController: NavController? = null
    scenario.onActivity {
        navController = it.findNavController(R.id.navHostFragment)
    }
    return navController ?: throw IllegalStateException("No nav controller")
}