package dev.ohoussein.restos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.ohoussein.restos.config.IAppFlavorSetup
import dev.ohoussein.restos.config.ReleaseAppSetup
import okhttp3.Interceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppExtensionModule {

    @Provides
    @Singleton
    fun provideAppFlavorSetup(): IAppFlavorSetup = ReleaseAppSetup()

    @Provides
    @Named(DIConstants.Qualifier.HTTP_NETWORK_INTERCEPTOR)
    @Singleton
    fun provideHttpNetworkInterceptors(): Array<Interceptor> = emptyArray()

    @Provides
    @Named(DIConstants.Qualifier.HTTP_INTERCEPTOR)
    @Singleton
    fun provideHttpInterceptors(): Array<Interceptor> = emptyArray()
}
