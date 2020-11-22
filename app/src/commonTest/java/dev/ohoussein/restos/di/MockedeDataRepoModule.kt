package dev.ohoussein.restos.di

import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.ohoussein.restos.domain.repo.IVenueRepository
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object MockedeDataRepoModule {

    @Provides
    @Singleton
    fun provideGitubRepository(): IVenueRepository = mock()
}
