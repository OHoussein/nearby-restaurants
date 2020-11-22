package dev.ohoussein.restos.ui.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiLocation(
        val coordinates: UiCoordinates,
        val address: String,
        val city: String?,
        val distance: Int,
) : Parcelable
