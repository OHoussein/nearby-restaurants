package dev.ohoussein.restos.data.mapper

import dev.ohoussein.restos.data.model.*
import dev.ohoussein.restos.domain.model.*

class DomainModelMapper {
    fun toDomain(dataModel: FSQCategory) = Category(
            name = dataModel.name,
            icon = dataModel.icon?.let { toDomain(it) },
            primary = dataModel.primary ?: false,
    )

    fun toDomain(dataModel: FSQIcon) = Icon(dataModel.prefix, dataModel.suffix)

    fun toDomain(dataModel: FSQLocation) = Location(
            coordinates = LocationCoordinates(dataModel.lat, dataModel.lng),
            address = dataModel.address,
            city = dataModel.city,
            distance = dataModel.distance,
    )

    fun toDomain(dataModel: FSQVenue) = Venue(
            id = dataModel.id,
            name = dataModel.name,
            location = toDomain(dataModel.location),
            categories = dataModel.categories.map { toDomain(it) },
    )

    fun toDomain(dataModel: FSQResponse<FSQVenueList>) =
            dataModel.response.venues.map { toDomain(it) }
}
