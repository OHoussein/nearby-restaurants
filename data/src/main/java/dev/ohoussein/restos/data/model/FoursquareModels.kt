package dev.ohoussein.restos.data.model

import com.squareup.moshi.Json

data class FSQResponse<T>(
        @field:Json(name = "response") val response: T,
)

data class FSQVenueList(
        @field:Json(name = "venues") val venues: List<FSQVenue>,
)

data class FSQVenue(
        @field:Json(name = "id") val id: String,
        @field:Json(name = "name") val name: String,
        @field:Json(name = "location") val location: FSQLocation,
        @field:Json(name = "categories") val categories: List<FSQCategory>,
)

data class FSQLocation(
        @field:Json(name = "address") val address: String?,
        @field:Json(name = "city") val city: String?,
        @field:Json(name = "lat") val lat: Double,
        @field:Json(name = "lng") val lng: Double,
        @field:Json(name = "distance") val distance: Int,
)

data class FSQCategory(
        @field:Json(name = "name") val name: String,
        @field:Json(name = "icon") val icon: FSQIcon?,
        @field:Json(name = "primary") val primary: Boolean?,
)

data class FSQIcon(
        @field:Json(name = "prefix") val prefix: String,
        @field:Json(name = "suffix") val suffix: String,
)


data class FSQGetVenuesParams(
        @field:Json(name = "ll") val latLng: String,
        @field:Json(name = "radius") val radius: Int,
        @field:Json(name = "limit") val limit: Int,
)
