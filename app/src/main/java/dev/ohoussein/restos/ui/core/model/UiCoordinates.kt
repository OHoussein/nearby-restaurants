package dev.ohoussein.restos.ui.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiCoordinates(
        val lat: Double,
        val lng: Double,
) : Parcelable
