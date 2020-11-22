@file:Suppress("MemberVisibilityCanBePrivate")

package dev.ohoussein.restos.mock

import dev.ohoussein.restos.domain.model.Location
import dev.ohoussein.restos.domain.model.LocationCoordinates
import dev.ohoussein.restos.domain.model.Venue
import java.util.concurrent.atomic.AtomicLong
import kotlin.random.Random

object TestDataFactory {
    private val idIndex = AtomicLong()

    fun createRandomVenueList(count: Int) = (1..count).map {
        createRandomVenue(it.toLong())
    }

    fun createRandomVenue(id: Long = idIndex.getAndIncrement()) =
            Venue(
                    id = id.toString(),
                    name = "Venue $id",
                    location = createRandomLocation(id),
                    categories = emptyList(),
            )

    fun createRandomLocation(id: Long = idIndex.getAndIncrement()) =
            Location(
                    coordinates = LocationCoordinates(
                            lat = 48.87 + Random.nextDouble(-1.0, 1.0),
                            lng = 50.0 + Random.nextDouble(-1.0, 1.0)
                    ),
                    address = "address $id",
                    city = "city $id",
                    distance = Random.nextInt(200),
            )
}
