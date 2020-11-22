package dev.ohoussein.restos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.ohoussein.restos.domain.repo.IVenueRepository
import dev.ohoussein.restos.domain.usecase.GetNearbyVenuesUseCase

@Module
@InstallIn(ApplicationComponent::class)
object DomainModule {

    @Provides
    fun provideGetTopRepositoriesUseCase(repo: IVenueRepository) = GetNearbyVenuesUseCase(repo)

}
