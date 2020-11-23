package dev.ohoussein.restos.data.network.interceptor

import dev.ohoussein.restos.data.Config
import okhttp3.Interceptor
import okhttp3.Response

class AddCredentialsInterceptor(
        private val clientId: String,
        private val sercret: String,
        private val apiVersion: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val newUrl = request
                .url()
                .newBuilder()
                .addQueryParameter(Config.API_CLIEND_ID_PARAM, clientId)
                .addQueryParameter(Config.API_SECRET_PARAM, sercret)
                .addQueryParameter(Config.API_VERSION_PARAM, apiVersion)
                .build()
        return chain.proceed(request.newBuilder()
                .url(newUrl)
                .build())
    }

}
