@file:Suppress("MemberVisibilityCanBePrivate")

package dev.ohoussein.restos.mock

import dev.ohoussein.restos.domain.model.Location
import dev.ohoussein.restos.domain.model.LocationCoordinates
import dev.ohoussein.restos.domain.model.Venue
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

object TestDataFactory {
    private val idIndex = AtomicLong()

    fun createRandomVenueList(
            count: Int,
            latCenter: Double = 40.0,
            lngCenter: Double = 50.0,
            delta: Double = 1.0,
    ) = (1..count).map {
        createRandomVenue(it.toLong(), latCenter, lngCenter, delta)
    }

    fun createRandomVenue(
            id: Long = idIndex.getAndIncrement(),
            latCenter: Double = 40.0,
            lngCenter: Double = 50.0,
            delta: Double = 1.0,
    ) = Venue(
            id = id.toString(),
            name = "Venue $id",
            location = createRandomLocation(id, latCenter, lngCenter, delta),
            categories = emptyList(),
    )

    fun createRandomLocation(id: Long = idIndex.getAndIncrement(),
                             latCenter: Double,
                             lngCenter: Double,
                             delta: Double) = Location(
            coordinates = LocationCoordinates(
                    lat = latCenter + Random.nextDouble(-delta, delta),
                    lng = lngCenter + Random.nextDouble(-delta, delta)
            ),
            address = "address $id",
            city = "city $id",
            distance = Random.nextInt(200),
    )
}
