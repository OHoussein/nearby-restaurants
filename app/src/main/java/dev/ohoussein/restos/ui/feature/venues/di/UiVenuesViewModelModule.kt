package dev.ohoussein.restos.ui.feature.venues.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dev.ohoussein.restos.common.coroutine.CoroutineContextProvider
import dev.ohoussein.restos.domain.usecase.GetNearbyVenuesUseCase
import dev.ohoussein.restos.ui.core.mapper.UiDomainModelMapper
import dev.ohoussein.restos.ui.feature.venues.viewmodel.NearbyRestaurantsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(ActivityRetainedComponent::class)
object UiVenuesViewModelModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun provideNearbyRestaurantsViewModel(coroutineContextProvider: CoroutineContextProvider,
                                          useCase: GetNearbyVenuesUseCase,
                                          entityMapper: UiDomainModelMapper): NearbyRestaurantsViewModel =
            NearbyRestaurantsViewModel(coroutineContextProvider, useCase, entityMapper)
}
