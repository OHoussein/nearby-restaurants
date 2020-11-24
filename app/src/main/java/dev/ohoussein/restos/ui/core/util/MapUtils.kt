package dev.ohoussein.restos.ui.core.util

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import dev.ohoussein.restos.ui.core.model.UiCoordinates
import dev.ohoussein.restos.ui.core.model.UiViewPort
import kotlin.math.abs
import kotlin.math.max

object MapUtils {


    fun LatLngBounds.toUiCoordinates() = UiViewPort(
            northEast = northEast.toUiCoordinates(),
            southWest = southWest.toUiCoordinates(),
            center = center.toUiCoordinates(),
            radius = radius()
    )

    fun LatLng.toUiCoordinates() = UiCoordinates(
            lat = latitude,
            lng = longitude
    )

    //TODO check this
    fun LatLngBounds.radius() = max(
            abs(center.latitude - abs(northEast.altitude)),
            abs(center.longitude - abs(northEast.longitude)),
    ).toInt()


    fun UiCoordinates.toLatLng() = LatLng(lat, lng)

    fun UiCoordinates.containedIn(viewPort: UiViewPort): Boolean {
        val latLngBounds = LatLngBounds.Builder()
                .include(LatLng(viewPort.northEast.toLatLng()))
                .include(LatLng(viewPort.southWest.toLatLng()))
                .build()
        return latLngBounds.contains(toLatLng())
    }
}
