package com.alexjprog.deezerforandroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModelStore
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListAdapter
import com.alexjprog.deezerforandroid.ui.adapter.tile.HorizontalTileListAdapter
import com.alexjprog.deezerforandroid.ui.mvvm.HomeFragment
import com.alexjprog.deezerforandroid.util.waitForVisibleView
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenNavInstrumentedTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun openMoreChartsTest() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setViewModelStore(ViewModelStore())

        launchFragmentInContainer(themeResId = R.style.Theme_DeezerForAndroid) {
            HomeFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.nav_graph)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        waitForVisibleView(withId(R.id.rcEditorialFeed), 10000).perform(
            RecyclerViewActions.scrollTo<ComplexListAdapter.TitleWithMoreActionViewHolder>(
                hasDescendant(withText("Charts"))
            )
        )

        onView(withText("Charts")).perform(click())
        assertEquals(R.id.moreContentFragment, navController.currentDestination?.id)
    }

    @Test
    fun openPlayerForFirstInChartsTest() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        navController.setViewModelStore(ViewModelStore())

        launchFragmentInContainer(themeResId = R.style.Theme_DeezerForAndroid) {
            HomeFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.nav_graph)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        waitForVisibleView(withId(R.id.rcEditorialFeed), 10000).perform(
            RecyclerViewActions.actionOnItem<ComplexListAdapter.HorizontalTrackListViewHolder>(
                withId(R.id.rcHorizontalList),
                RecyclerViewActions.actionOnItemAtPosition<HorizontalTileListAdapter.HorizontalTileViewHolder>(
                    0,
                    click()
                )
            )
        )

        assertEquals(R.id.playerFragment, navController.currentDestination?.id)
    }
}