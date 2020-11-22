package dev.ohoussein.restos.domain.repo

import dev.ohoussein.restos.domain.model.GetVenuesParams
import dev.ohoussein.restos.domain.model.Venue
import kotlinx.coroutines.flow.Flow

interface IVenueRepository {

    fun getVenues(query: GetVenuesParams): Flow<List<Venue>>
}
