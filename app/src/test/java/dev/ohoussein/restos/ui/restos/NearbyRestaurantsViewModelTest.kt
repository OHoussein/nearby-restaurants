package dev.ohoussein.restos.ui.restos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.ohoussein.restos.core.TestCoroutineContextProvider
import dev.ohoussein.restos.core.delayedFlowOf
import dev.ohoussein.restos.core.getOrAwaitValue
import dev.ohoussein.restos.domain.model.Venue
import dev.ohoussein.restos.domain.usecase.GetNearbyVenuesUseCase
import dev.ohoussein.restos.mock.TestDataFactory
import dev.ohoussein.restos.rule.TestCoroutineRule
import dev.ohoussein.restos.ui.core.mapper.UiDomainModelMapper
import dev.ohoussein.restos.ui.core.model.UiCoordinates
import dev.ohoussein.restos.ui.core.model.UiResource
import dev.ohoussein.restos.ui.core.model.UiVenue
import dev.ohoussein.restos.ui.core.model.UiViewPort
import dev.ohoussein.restos.ui.feature.venues.viewmodel.NearbyRestaurantsViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.IOException


@ExperimentalCoroutinesApi
class NearbyRestaurantsViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var tested: NearbyRestaurantsViewModel
    private lateinit var useCase: GetNearbyVenuesUseCase

    private val uiMapper = UiDomainModelMapper()
    private val mockRepoListObserver = mock<Observer<UiResource<List<UiVenue>>>>()

    private val viewPort = UiViewPort(
            northEast = UiCoordinates(40.1, 2.0),
            southWest = UiCoordinates(40.0, 2.1),
            center = UiCoordinates(40.05, 2.05),
            radius = 500,
    )

    private val apiTimeout = 500L

    @Before
    fun setup() {
        useCase = mock()
        //TODO this is workoround because TestCoroutineScope  doesn't block the debounce
        tested = NearbyRestaurantsViewModel(
                TestCoroutineContextProvider(testCoroutineRule.testCoroutineDispatcher),
                useCase,
                uiMapper,
        )
    }

    @After
    fun cleanup() {
        testCoroutineRule.testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should load venue list`() = testCoroutineRule.testCoroutineDispatcher.runBlockingTest {
        //Given
        val listVenue = TestDataFactory.createRandomVenueList(10)
        val uiData = listVenue.map { uiMapper.toUiModel(it) }

        whenever(useCase.get(any())).thenReturn(flowOf(listVenue))
        //When
        tested.restaurantList.observeForever(mockRepoListObserver)
        tested.updateViewPort(viewPort)
        //Then
        advanceTimeBy(NearbyRestaurantsViewModel.DEBOUNCE_TIMER)
        verify(mockRepoListObserver).onChanged(any<UiResource.Loading<List<UiVenue>>>())
        verify(mockRepoListObserver).onChanged(UiResource.Success(uiData))
    }

    @Test
    fun `should get error when loading venue list`() = testCoroutineRule.testCoroutineDispatcher.runBlockingTest {
        //Given
        val error = IOException("")
        val dataFlow = flow<List<Venue>> { throw error }
        whenever(useCase.get(any())).thenReturn(dataFlow)
        //When
        tested.restaurantList.observeForever(mockRepoListObserver)
        tested.updateViewPort(viewPort)
        //Then
        advanceTimeBy(NearbyRestaurantsViewModel.DEBOUNCE_TIMER)
        verify(mockRepoListObserver).onChanged(any<UiResource.Loading<List<UiVenue>>>())
        verify(mockRepoListObserver).onChanged(any<UiResource.Error<List<UiVenue>>>())
    }

    @Test
    fun `should get cache when loading`() = testCoroutineRule.testCoroutineDispatcher.runBlockingTest {
        //Given
        val smallerViewPort = UiViewPort(
                northEast = UiCoordinates(40.1, 2.0),
                southWest = UiCoordinates(40.0, 2.1),
                center = UiCoordinates(40.05, 2.05),
                radius = 500,
        )
        val smallerListVenue = TestDataFactory.createRandomVenueList(
                count = 5,
                smallerViewPort.center.lat,
                smallerViewPort.center.lng,
                delta = 0.01,
        )
        val largerViewPort = UiViewPort(
                northEast = UiCoordinates(40.2, 2.0),
                southWest = UiCoordinates(40.0, 2.2),
                center = UiCoordinates(40.05, 2.05),
                radius = 500,
        )
        val largerListVenue = TestDataFactory.createRandomVenueList(
                count = 10,
                largerViewPort.center.lat,
                largerViewPort.center.lng,
                delta = 0.01,
        )

        whenever(useCase.get(any()))
                .thenReturn(delayedFlowOf(smallerListVenue, apiTimeout))
                .thenReturn(delayedFlowOf(largerListVenue, apiTimeout))

        //When
        tested.updateViewPort(smallerViewPort)

        val firstLoading = tested.restaurantList.getOrAwaitValue {
            advanceTimeBy(NearbyRestaurantsViewModel.DEBOUNCE_TIMER)
        }
        advanceTimeBy(apiTimeout)
        val firstSuccess = tested.restaurantList.getOrAwaitValue()
        tested.updateViewPort(largerViewPort)
        advanceTimeBy(NearbyRestaurantsViewModel.DEBOUNCE_TIMER)
        val secondLoading = tested.restaurantList.getOrAwaitValue()
        advanceTimeBy(apiTimeout)
        val secondSuccess = tested.restaurantList.getOrAwaitValue()

        //Then
        assertThat(firstLoading, instanceOf(UiResource.Loading::class.java))
        assertThat(firstSuccess, instanceOf(UiResource.Success::class.java))
        assertThat(secondLoading, instanceOf(UiResource.Loading::class.java))
        assertEquals(smallerListVenue.size, (secondLoading as UiResource.Loading<List<UiVenue>>).data?.size)
        assertThat(secondSuccess, instanceOf(UiResource.Success::class.java))
    }
}