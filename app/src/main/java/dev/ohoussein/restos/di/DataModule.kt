package dev.ohoussein.restos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.ohoussein.restos.data.mapper.DomainModelMapper
import dev.ohoussein.restos.data.network.ApiFSQService
import dev.ohoussein.restos.data.network.NetworkBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object DataModule {

    @Provides
    fun provideConverter(): Converter.Factory = NetworkBuilder.createConverter()

    @Provides
    @Singleton
    fun provideOkHttp(
            @Named(DIConstants.Qualifier.API_CLIENT_ID) clientId: String,
            @Named(DIConstants.Qualifier.API_CLIENT_SECRET) clientSecret: String,
            @Named(DIConstants.Qualifier.HTTP_INTERCEPTOR) httpInterceptor: Array<Interceptor>,
            @Named(DIConstants.Qualifier.HTTP_NETWORK_INTERCEPTOR) httpNetworkInterceptor: Array<Interceptor>,
    ): OkHttpClient = NetworkBuilder.createOkHttp(clientId, clientSecret, httpInterceptor, httpNetworkInterceptor)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit =
            NetworkBuilder.createRetrofit(
                    okHttpClient = okHttpClient,
                    converterFactory = converterFactory)

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiFSQService = NetworkBuilder.createApiService(retrofit)

    @Provides
    fun provideDomainEntityMapper() = DomainModelMapper()

}
