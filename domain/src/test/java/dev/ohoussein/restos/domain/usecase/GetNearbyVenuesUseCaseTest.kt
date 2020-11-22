package dev.ohoussein.restos.domain.usecase

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.ohoussein.restos.domain.mock.TestDataFactory
import dev.ohoussein.restos.domain.model.Venue
import dev.ohoussein.restos.domain.repo.IVenueRepository
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class GetNearbyVenuesUseCaseTest {

    lateinit var tested: GetNearbyVenuesUseCase
    lateinit var remoteRepository: IVenueRepository

    @Before
    fun setup() {
        remoteRepository = mock()
        tested = GetNearbyVenuesUseCase(remoteRepository)
    }

    @Test
    fun `should call get venues`() {
        //Given
        val listVenues = mock<List<Venue>>()
        val query = TestDataFactory.createGetVenuesParams()
        val dataFlow = flowOf(listVenues)
        whenever(remoteRepository.getVenues(query)).thenReturn(dataFlow)
        //When
        tested.get(query)
        //Then
        verify(remoteRepository).getVenues(query)
    }
}
