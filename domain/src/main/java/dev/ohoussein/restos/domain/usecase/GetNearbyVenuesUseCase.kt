package dev.ohoussein.restos.domain.usecase

import dev.ohoussein.restos.domain.model.GetVenuesParams
import dev.ohoussein.restos.domain.repo.IVenueRepository


class GetNearbyVenuesUseCase(private val remoteRepo: IVenueRepository) {

    fun get(query: GetVenuesParams) = remoteRepo.getVenues(query)
}
