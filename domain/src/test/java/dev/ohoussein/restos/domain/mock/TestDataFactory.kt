package dev.ohoussein.restos.domain.mock

import dev.ohoussein.restos.domain.model.GetVenuesParams
import dev.ohoussein.restos.domain.model.LocationCoordinates
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt

object TestDataFactory {

    fun createGetVenuesParams() = GetVenuesParams(
            coordinates = LocationCoordinates(lat = nextDouble(-90.0, 90.0), lng = nextDouble(-180.0, 80.0)),
            radius = nextInt(10, 50),
            limit = 20,
    )
}
