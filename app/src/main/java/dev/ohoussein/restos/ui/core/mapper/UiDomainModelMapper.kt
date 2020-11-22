package dev.ohoussein.restos.ui.core.mapper

import dev.ohoussein.restos.config.UiParams
import dev.ohoussein.restos.domain.model.Category
import dev.ohoussein.restos.domain.model.Location
import dev.ohoussein.restos.domain.model.LocationCoordinates
import dev.ohoussein.restos.domain.model.Venue
import dev.ohoussein.restos.ui.core.model.UiCategory
import dev.ohoussein.restos.ui.core.model.UiCoordinates
import dev.ohoussein.restos.ui.core.model.UiLocation
import dev.ohoussein.restos.ui.core.model.UiVenue

class UiDomainModelMapper {

    fun toUiModel(domainModel: Category) = UiCategory(
            name = domainModel.name,
            iconUrl = domainModel.icon?.getUrl(UiParams.categoryIconSize),
            primary = domainModel.primary,
    )

    fun toUiModel(domainModel: Location) = UiLocation(
            coordinates = UiCoordinates(domainModel.coordinates.lat, domainModel.coordinates.lng),
            address = domainModel.address,
            city = domainModel.city,
            distance = domainModel.distance,
    )

    fun toUiModel(domainModel: Venue) = UiVenue(
            id = domainModel.id,
            name = domainModel.name,
            location = toUiModel(domainModel.location),
            categories = domainModel.categories.map { toUiModel(it) },
    )

    fun toDomain(uiModel: UiCoordinates) = LocationCoordinates(uiModel.lat, uiModel.lng)
}
