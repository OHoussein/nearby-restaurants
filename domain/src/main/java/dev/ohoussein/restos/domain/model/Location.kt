package dev.ohoussein.restos.domain.model

data class Location(
        val coordinates: LocationCoordinates,
        val address: String,
        val city: String?,
        val distance: Int,
)
