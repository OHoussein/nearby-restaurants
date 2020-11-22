package dev.ohoussein.restos.domain.model

data class GetVenuesParams(
        val coordinates: LocationCoordinates,
        val radius: Int,
        val limit: Int,
)
