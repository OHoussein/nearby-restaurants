package dev.ohoussein.restos.data.util

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.File

object NetworkUtils {

    fun readMockFile(fileName: String): String {
        val fileUri = javaClass.classLoader!!.getResource(fileName)
        return File(fileUri.path).readText()
    }

    fun MockWebServer.withReponse(response: MockResponse) {
        this.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return response
            }
        }
    }
}

