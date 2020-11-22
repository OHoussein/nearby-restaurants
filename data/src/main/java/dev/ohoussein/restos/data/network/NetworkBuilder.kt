package dev.ohoussein.restos.data.network

import dev.ohoussein.restos.data.BuildConfig
import dev.ohoussein.restos.data.Config
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkBuilder {
    fun createConverter(): Converter.Factory = MoshiConverterFactory.create()

    fun createOkHttp(httpInterceptor: Array<Interceptor>, httpNetWorkInterceptor: Array<Interceptor>): OkHttpClient =
            OkHttpClient.Builder()
                    .apply {
                        httpInterceptor.forEach {
                            addInterceptor(it)
                        }
                        httpNetWorkInterceptor.forEach {
                            addNetworkInterceptor(it)
                        }
                    }
                    .build()

    fun createRetrofit(baseUrl: HttpUrl = HttpUrl.get(Config.API_BASE_URL),
                       okHttpClient: OkHttpClient = createOkHttp(emptyArray(), emptyArray()),
                       converterFactory: Converter.Factory = createConverter()): Retrofit =
            Retrofit.Builder()
                    .addConverterFactory(converterFactory)
                    .baseUrl(baseUrl)
                    .validateEagerly(BuildConfig.DEBUG)
                    .client(okHttpClient)
                    .build()

    fun createApiService(retrofit: Retrofit = createRetrofit()): ApiFSQService =
            retrofit.create(ApiFSQService::class.java)
}
