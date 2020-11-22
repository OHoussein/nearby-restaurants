package dev.ohoussein.restos.ui.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiViewPort(
        val northEast: UiCoordinates,
        val southWest: UiCoordinates,
        //TODO we can remove the bellow attrs
        val center: UiCoordinates,
        val radius: Int,
) : Parcelable
