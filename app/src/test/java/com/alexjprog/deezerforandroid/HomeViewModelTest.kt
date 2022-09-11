package com.alexjprog.deezerforandroid

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alexjprog.deezerforandroid.domain.usecase.GetChartsUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetFlowUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetRecommendationsUseCase
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
import com.alexjprog.deezerforandroid.util.CHARTS_PREVIEW_SIZE
import com.alexjprog.deezerforandroid.util.FLOW_PREVIEW_SIZE
import com.alexjprog.deezerforandroid.util.RECOMMENDATIONS_PREVIEW_SIZE
import com.alexjprog.deezerforandroid.viewmodel.HomeViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var getChartsUseCase: GetChartsUseCase

    @MockK
    lateinit var getFlowUseCase: GetFlowUseCase

    @MockK
    lateinit var getRecommendationsUseCase: GetRecommendationsUseCase

    private lateinit var homeViewModel: HomeViewModel

    private val coroutineContext = UnconfinedTestDispatcher()

    @Before
    fun beforeTest() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testSuccessfulLoad() {
        val captorChartsSize = slot<Int>()
        val captorFlowSize = slot<Int>()
        val captorRecommendationsSize = slot<Int>()
        coEvery { getChartsUseCase(capture(captorChartsSize)) } answers {
            generateTrackFlow(
                captorChartsSize.captured
            )
        }
        coEvery { getFlowUseCase(capture(captorFlowSize)) } answers {
            generateTrackFlow(
                captorFlowSize.captured
            )
        }
        coEvery { getRecommendationsUseCase(capture(captorRecommendationsSize)) } answers {
            generateTrackFlow(
                captorRecommendationsSize.captured
            )
        }

        //loadFeed() called in constructor
        homeViewModel = HomeViewModel(
            getChartsUseCase,
            getFlowUseCase,
            getRecommendationsUseCase,
            coroutineContext
        )
        val homeFeed = homeViewModel.feed.getOrAwaitValue()

        Assert.assertNotNull("Home feed is null", homeFeed)
        MatcherAssert.assertThat(
            homeFeed,
            hasItem(ComplexListItem.TitleItemWithOpenMoreAction(ContentCategory.CHARTS))
        )
        MatcherAssert.assertThat(
            homeFeed,
            hasItem(ComplexListItem.TitleItemWithOpenMoreAction(ContentCategory.FLOW))
        )
        MatcherAssert.assertThat(
            homeFeed,
            hasItem(ComplexListItem.TitleItemWithOpenMoreAction(ContentCategory.RECOMMENDATIONS))
        )

        coVerify(exactly = 1) { getChartsUseCase(CHARTS_PREVIEW_SIZE) }
        coVerify(exactly = 1) { getFlowUseCase(FLOW_PREVIEW_SIZE) }
        coVerify(exactly = 1) { getRecommendationsUseCase(RECOMMENDATIONS_PREVIEW_SIZE) }

        confirmVerified(getChartsUseCase, getFlowUseCase, getRecommendationsUseCase)
    }

    @Test
    fun testUnsuccessfulLoad() {
        coEvery { getChartsUseCase(any()) } returns generateFlowWithException()
        coEvery { getFlowUseCase(any()) } returns generateFlowWithException()
        coEvery { getRecommendationsUseCase(any()) } returns generateFlowWithException()

        //loadFeed() called in constructor
        homeViewModel = HomeViewModel(
            getChartsUseCase,
            getFlowUseCase,
            getRecommendationsUseCase,
            coroutineContext
        )
        val homeFeed = homeViewModel.feed.getOrAwaitValue()

        Assert.assertNull("Home feed is not null", homeFeed)
        MatcherAssert.assertThat(
            homeFeed,
            not(hasItem(ComplexListItem.TitleItemWithOpenMoreAction(ContentCategory.CHARTS)))
        )
        MatcherAssert.assertThat(
            homeFeed,
            not(hasItem(ComplexListItem.TitleItemWithOpenMoreAction(ContentCategory.FLOW)))
        )
        MatcherAssert.assertThat(
            homeFeed,
            not(hasItem(ComplexListItem.TitleItemWithOpenMoreAction(ContentCategory.RECOMMENDATIONS)))
        )

        coVerify(exactly = 1) { getChartsUseCase(CHARTS_PREVIEW_SIZE) }
        coVerify(exactly = 1) { getFlowUseCase(FLOW_PREVIEW_SIZE) }
        coVerify(exactly = 1) { getRecommendationsUseCase(RECOMMENDATIONS_PREVIEW_SIZE) }

        confirmVerified(getChartsUseCase, getFlowUseCase, getRecommendationsUseCase)
    }

    @Test
    fun testOneUseCaseFails() {
        val captorChartsSize = slot<Int>()
        val captorFlowSize = slot<Int>()
        coEvery { getChartsUseCase(capture(captorChartsSize)) } answers {
            generateTrackFlow(
                captorChartsSize.captured
            )
        }
        coEvery { getFlowUseCase(capture(captorFlowSize)) } answers {
            generateTrackFlow(
                captorFlowSize.captured
            )
        }
        coEvery { getRecommendationsUseCase(any()) } returns generateFlowWithException()

        //loadFeed() called in constructor
        homeViewModel = HomeViewModel(
            getChartsUseCase,
            getFlowUseCase,
            getRecommendationsUseCase,
            coroutineContext
        )
        val homeFeed = homeViewModel.feed.getOrAwaitValue()

        Assert.assertNull("Home feed is not null", homeFeed)
        MatcherAssert.assertThat(
            homeFeed,
            not(hasItem(ComplexListItem.TitleItemWithOpenMoreAction(ContentCategory.CHARTS)))
        )
        MatcherAssert.assertThat(
            homeFeed,
            not(hasItem(ComplexListItem.TitleItemWithOpenMoreAction(ContentCategory.FLOW)))
        )
        MatcherAssert.assertThat(
            homeFeed,
            not(hasItem(ComplexListItem.TitleItemWithOpenMoreAction(ContentCategory.RECOMMENDATIONS)))
        )

        coVerify(exactly = 1) { getChartsUseCase(CHARTS_PREVIEW_SIZE) }
        coVerify(exactly = 1) { getFlowUseCase(FLOW_PREVIEW_SIZE) }
        coVerify(exactly = 1) { getRecommendationsUseCase(RECOMMENDATIONS_PREVIEW_SIZE) }

        confirmVerified(getChartsUseCase, getFlowUseCase, getRecommendationsUseCase)
    }
}