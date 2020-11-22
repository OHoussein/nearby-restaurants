package dev.ohoussein.restos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.ohoussein.restos.common.coroutine.CoroutineContextProvider
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideCoroutineContextProvider(): CoroutineContextProvider = CoroutineContextProvider()
}
