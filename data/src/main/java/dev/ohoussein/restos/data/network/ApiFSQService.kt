package dev.ohoussein.restos.data.network

import dev.ohoussein.restos.data.model.FSQResponse
import dev.ohoussein.restos.data.model.FSQVenueList
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiFSQService {

    @GET("v2/venues/search")
    suspend fun searchVenues(
            @Query("ll") latLng: String,
            @Query("radius") radius: Int,
            @Query("limit") limit: Int,
    ): FSQResponse<FSQVenueList>
}
