package dev.ohoussein.restos.ui.core.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import dev.ohoussein.restos.ui.core.model.UiLocation

object ActivityUtils {

    fun Activity.openMapsOn(to: UiLocation) {
        val destination = to.address ?: "${to.coordinates.lat},${to.coordinates.lng}"
        val gmmIntentUri = Uri.parse("google.navigation:q=${destination}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }
}
