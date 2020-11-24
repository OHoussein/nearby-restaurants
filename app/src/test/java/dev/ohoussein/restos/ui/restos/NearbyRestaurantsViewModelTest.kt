package dev.ohoussein.restos.ui.restos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.ohoussein.restos.core.TestCoroutineContextProvider
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
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

    @Before
    fun setup() {
        useCase = mock()
        //TODO this is workoround because TestCoroutineScope  doesn't block the debounce
        NearbyRestaurantsViewModel.debounceTimer = 0
        tested = NearbyRestaurantsViewModel(
                TestCoroutineContextProvider(testCoroutineRule.testCoroutineDispatcher),
                useCase,
                uiMapper,
        )
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
        verify(mockRepoListObserver).onChanged(any<UiResource.Loading<List<UiVenue>>>())
        verify(mockRepoListObserver).onChanged(any<UiResource.Error<List<UiVenue>>>())
    }

    //TODO fix this unit test, another dispatcher issue on testing
/*
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
                delta = 1.0,
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
                delta = 1.0,
        )

        val firstListVenue = largerListVenue + smallerListVenue

        tested.restaurantList.observeForever(mockRepoListObserver)

        whenever(useCase.get(any())).thenReturn(flowOf(firstListVenue))
        tested.updateViewPort(largerViewPort)

        whenever(useCase.get(any())).thenReturn(flowOf(smallerListVenue))
        tested.updateViewPort(smallerViewPort)

        //Then
        verify(mockRepoListObserver).onChanged(any<UiResource.Loading<List<UiVenue>>>())

        argumentCaptor<UiResource<List<UiVenue>>>().apply {
            verify(mockRepoListObserver, times(4)).onChanged(capture())
            assertTrue(firstValue is UiResource.Loading)
            assertTrue(secondValue is UiResource.Success)

            val successData = secondValue as UiResource.Success
            assertEquals(firstListVenue.size, successData.data.size)

            assertTrue(thirdValue is UiResource.Loading)
            val secondLoadingData = thirdValue as UiResource.Loading
            assertNotNull(secondLoadingData.data)
            assertEquals(smallerListVenue.size, secondLoadingData.data?.size)
        }

    }*/
}
