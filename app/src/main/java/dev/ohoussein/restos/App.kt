package dev.ohoussein.restos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.ohoussein.restos.config.IAppFlavorSetup
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var appSetup: IAppFlavorSetup

    override fun onCreate() {
        super.onCreate()

        appSetup.setup(this)
    }
}
