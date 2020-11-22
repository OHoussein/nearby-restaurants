package dev.ohoussein.restos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.ohoussein.restos.common.coroutine.CoroutineContextProvider
import dev.ohoussein.restos.core.TestCoroutineContextProvider
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object TestCoreModule {
    @Provides
    @Singleton
    fun provideCoroutineContextProvider(): CoroutineContextProvider = TestCoroutineContextProvider()
}
