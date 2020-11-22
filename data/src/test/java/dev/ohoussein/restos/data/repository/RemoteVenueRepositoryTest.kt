package dev.ohoussein.restos.data.repository

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.ohoussein.restos.data.mapper.DomainModelMapper
import dev.ohoussein.restos.data.model.FSQResponse
import dev.ohoussein.restos.data.model.FSQVenueList
import dev.ohoussein.restos.data.network.ApiFSQService
import dev.ohoussein.restos.domain.model.GetVenuesParams
import dev.ohoussein.restos.domain.model.LocationCoordinates
import dev.ohoussein.restos.domain.model.Venue
import dev.ohoussein.restos.domain.repo.IVenueRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.contains
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class RemoteVenueRepositoryTest {

    private lateinit var repository: IVenueRepository

    @Mock
    private lateinit var apiService: ApiFSQService

    @Mock
    private lateinit var mapper: DomainModelMapper

    @Before
    fun setup() {
        repository = RemoteVenueRepository(apiService, mapper)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getVenueList() = runBlockingTest {
        //Given
        val venueListResponse = mock<FSQResponse<FSQVenueList>>()
        val venueList = mock<List<Venue>>()
        val query = createGetVenuesParams()

        whenever(
                apiService.searchVenues(contains(query.coordinates.lat.toString()), eq(query.radius), eq(query.limit))
        )
                .thenReturn(venueListResponse)
        whenever(mapper.toDomain(venueListResponse)).thenReturn(venueList)
        //When
        val result = repository.getVenues(query).first()
        //Then
        assertNotNull(result)
        assertEquals(result, venueList)
    }


    private fun createGetVenuesParams() = GetVenuesParams(
            coordinates = LocationCoordinates(
                    lat = Random.nextDouble(-90.0, 90.0),
                    lng = Random.nextDouble(-180.0, 80.0)),
            radius = Random.nextInt(10, 50),
            limit = 20,
    )
}
