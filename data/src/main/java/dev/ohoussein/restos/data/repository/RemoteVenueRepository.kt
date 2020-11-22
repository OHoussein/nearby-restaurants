package dev.ohoussein.restos.data.repository

import dev.ohoussein.restos.data.mapper.DomainModelMapper
import dev.ohoussein.restos.data.model.FSQResponse
import dev.ohoussein.restos.data.model.FSQVenueList
import dev.ohoussein.restos.data.network.ApiFSQService
import dev.ohoussein.restos.domain.model.GetVenuesParams
import dev.ohoussein.restos.domain.model.Venue
import dev.ohoussein.restos.domain.repo.IVenueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteVenueRepository(private val service: ApiFSQService,
                            private val mapper: DomainModelMapper) : IVenueRepository {

    override fun getVenues(query: GetVenuesParams): Flow<List<Venue>> = flow {
        val response: FSQResponse<FSQVenueList> = service.searchVenues(
                latLng = "${query.coordinates.lat},${query.coordinates.lng}",
                radius = query.radius,
                limit = query.limit,
        )
        val domainData = mapper.toDomain(response)
        emit(domainData)
    }
}
