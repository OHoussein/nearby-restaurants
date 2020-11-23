package dev.ohoussein.restos.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.ohoussein.restos.R
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object CredentialsModule {

    @Named(DIConstants.Qualifier.API_CLIENT_ID)
    @Provides
    fun provideClientId(@ApplicationContext context: Context) = context.getString(R.string.fsq_client_id)

    @Named(DIConstants.Qualifier.API_CLIENT_SECRET)
    @Provides
    fun provideClientSecret(@ApplicationContext context: Context) = context.getString(R.string.fsq_secret)
}
