package dev.ohoussein.restos.data.network

import dev.ohoussein.restos.data.util.NetworkUtils
import dev.ohoussein.restos.data.util.NetworkUtils.withReponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class ApiFSQServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: ApiFSQService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        service = NetworkBuilder
                .createRetrofit(baseUrl = mockWebServer.url("/"))
                .create(ApiFSQService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    //Bug: runBlockingTest doesn't work anymore here
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun searchVenuesTest() = runBlocking {
        mockWebServer.withReponse(
                MockResponse()
                        .setResponseCode(200)
                        .setBody(NetworkUtils.readMockFile("mock_search_venues.json")))

        val response = service.searchVenues("", 0, 0)

        assertNotNull(response)
        assertEquals(1, response.response.venues.size)

        val firstVenue = response.response.venues.first()

        assertEquals("Mr. Purple", firstVenue.name)
        assertEquals(1, firstVenue.categories.size)
        assertEquals("Hotel Bar", firstVenue.categories.first().name)
        assertEquals(40.72173744277209, firstVenue.location.lat, 0.0)
        assertEquals(-73.98800687282996, firstVenue.location.lng, 0.0)
    }
}
