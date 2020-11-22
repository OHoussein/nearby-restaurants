package dev.ohoussein.restos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.ohoussein.restos.data.mapper.DomainModelMapper
import dev.ohoussein.restos.data.network.ApiFSQService
import dev.ohoussein.restos.data.repository.RemoteVenueRepository
import dev.ohoussein.restos.domain.repo.IVenueRepository
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object DataRepoModule {

    @Provides
    @Singleton
    fun provideGitubRepository(service: ApiFSQService, mapper: DomainModelMapper): IVenueRepository =
            RemoteVenueRepository(service, mapper)

}
