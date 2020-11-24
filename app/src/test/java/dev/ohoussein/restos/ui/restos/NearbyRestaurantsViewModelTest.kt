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
            northEast = UiCoordinates(48.87382636834763, 2.3251443457666863),
            southWest = UiCoordinates(48.849191481820704, 2.3534122470179555),
            center = UiCoordinates(48.86215448593467, 2.333098517114446),
            radius = 500,
    )

    @Before
    fun setup() {
        useCase = mock()
        tested = NearbyRestaurantsViewModel(
                TestCoroutineContextProvider(testCoroutineRule.testCoroutineDispatcher),
                useCase,
                uiMapper,
        )
        tested.debounceTimer = 0
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
    fun `should get error when loading venue list`() {
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
}
