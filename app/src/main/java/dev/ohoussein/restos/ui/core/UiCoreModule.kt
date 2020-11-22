package dev.ohoussein.restos.ui.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.ohoussein.restos.ui.core.mapper.UiDomainModelMapper

@Module
@InstallIn(ApplicationComponent::class)
object UiCoreModule {

    @Provides
    fun provideUiDomainEntityMapper() = UiDomainModelMapper()
}
