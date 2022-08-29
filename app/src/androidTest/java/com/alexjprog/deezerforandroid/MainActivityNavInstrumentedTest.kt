package com.alexjprog.deezerforandroid

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexjprog.deezerforandroid.ui.MainActivity
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListAdapter
import com.alexjprog.deezerforandroid.util.getNavController
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
        val navController = mainActivityScenarioRule.getNavController()

        onView(withContentDescription(R.string.editorial)).perform(click())
        assertEquals(R.id.editorialFragment, navController.currentDestination?.id)

        pressBack()
        assertEquals(R.id.homeFragment, navController.currentDestination?.id)
    }

    @Test
    fun openEditorialFromMoreContentOpenedInHomeAndGoBack() {
        val navController = mainActivityScenarioRule.getNavController()

        waitForVisibleView(withId(R.id.rcHomeFeed)).perform(
            RecyclerViewActions.scrollTo<ComplexListAdapter.TitleWithMoreActionViewHolder>(
                hasDescendant(withText("Charts"))
            )
        )

        onView(withText("Charts")).perform(click())
        assertEquals(R.id.moreContentFragment, navController.currentDestination?.id)

        onView(withContentDescription(R.string.editorial)).perform(click())
        assertEquals(R.id.editorialFragment, navController.currentDestination?.id)

        pressBack()
        assertEquals(R.id.moreContentFragment, navController.currentDestination?.id)

        pressBack()
        assertEquals(R.id.homeFragment, navController.currentDestination?.id)
    }

    @Test
    fun openSearchAndGoBack() {
        val navController = mainActivityScenarioRule.getNavController()

        waitForVisibleView(withId(R.id.rcHomeFeed)).perform(
            RecyclerViewActions.scrollTo<ComplexListAdapter.TitleWithMoreActionViewHolder>(
                hasDescendant(withText("Charts"))
            )
        )

        onView(withContentDescription(R.string.search)).perform(click())
        assertEquals(R.id.searchFragment, navController.currentDestination?.id)

        pressBack()
        assertEquals(R.id.homeFragment, navController.currentDestination?.id)
    }

    @Test
    fun openMoreContentThenSearchThenEnterQueryThenEditorialAndGoBack() {
        val navController = mainActivityScenarioRule.getNavController()

        waitForVisibleView(withId(R.id.rcHomeFeed)).perform(
            RecyclerViewActions.scrollTo<ComplexListAdapter.TitleWithMoreActionViewHolder>(
                hasDescendant(withText("Charts"))
            )
        )

        onView(withText("Charts")).perform(click())
        assertEquals(R.id.moreContentFragment, navController.currentDestination?.id)

        onView(withContentDescription(R.string.search)).perform(click())
        assertEquals(R.id.searchFragment, navController.currentDestination?.id)

        onView(withId(R.id.etSearch))
            .perform(typeTextIntoFocusedView("some track"))
            .perform(pressImeActionButton())
        assertEquals(R.id.searchResultsFragment, navController.currentDestination?.id)

        waitForVisibleView(withContentDescription(R.string.editorial))
        onView(withContentDescription(R.string.editorial)).perform(click())
        assertEquals(R.id.editorialFragment, navController.currentDestination?.id)

        pressBack()
        assertEquals(R.id.searchResultsFragment, navController.currentDestination?.id)

        pressBack()
        assertEquals(R.id.moreContentFragment, navController.currentDestination?.id)

        pressBack()
        assertEquals(R.id.homeFragment, navController.currentDestination?.id)
    }
}