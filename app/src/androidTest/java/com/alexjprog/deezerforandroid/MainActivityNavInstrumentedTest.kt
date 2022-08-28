package com.alexjprog.deezerforandroid

import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexjprog.deezerforandroid.ui.MainActivity
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListAdapter
import com.alexjprog.deezerforandroid.util.waitForVisibleView
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityNavInstrumentedTest {

    @get:Rule
    val mainActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun openEditorialAndGoBack() {
        var navController: NavController? = null
        mainActivityScenarioRule.scenario.onActivity {
            navController = it.findNavController(R.id.navHostFragment)
        }

        onView(withContentDescription(R.string.editorial)).perform(click())
        assertEquals(R.id.editorialFragment, navController?.currentDestination?.id)

        pressBack()
        assertEquals(R.id.homeFragment, navController?.currentDestination?.id)
    }

    @Test
    fun openEditorialFromMoreContentOpenedInHomeAndGoBack() {
        var navController: NavController? = null
        mainActivityScenarioRule.scenario.onActivity {
            navController = it.findNavController(R.id.navHostFragment)
        }

        waitForVisibleView(withId(R.id.rcHomeFeed)).perform(
            RecyclerViewActions.scrollTo<ComplexListAdapter.TitleWithMoreActionViewHolder>(
                hasDescendant(withText("Charts"))
            )
        )

        onView(withText("Charts")).perform(click())
        assertEquals(R.id.moreContentFragment, navController?.currentDestination?.id)

        onView(withContentDescription(R.string.editorial)).perform(click())
        assertEquals(R.id.editorialFragment, navController?.currentDestination?.id)

        pressBack()
        assertEquals(R.id.moreContentFragment, navController?.currentDestination?.id)

        pressBack()
        assertEquals(R.id.homeFragment, navController?.currentDestination?.id)
    }
}