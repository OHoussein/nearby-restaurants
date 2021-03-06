package dev.ohoussein.restos.ui.restos

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.ohoussein.restos.di.CoreModule
import dev.ohoussein.restos.di.DataRepoModule
import dev.ohoussein.restos.domain.repo.IVenueRepository
import dev.ohoussein.restos.ui.feature.venues.activity.RestaurantsActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@HiltAndroidTest
@UninstallModules(value = [CoreModule::class, DataRepoModule::class])
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RestaurantsActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    internal lateinit var remoteVenueRepo: IVenueRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    //TODO add tests here
    @Test
    fun launchTest() {
        launchActivity<RestaurantsActivity>().use { _ ->
            //Then
        }
    }
}
