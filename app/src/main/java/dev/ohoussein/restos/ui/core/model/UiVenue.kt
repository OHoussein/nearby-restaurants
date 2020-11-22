package dev.ohoussein.restos.ui.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiVenue(
        val id: String,
        val name: String,
        val location: UiLocation,
        val categories: List<UiCategory>
) : Parcelable
