package dev.ohoussein.restos.ui.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UiCategory(
        val name: String,
        val iconUrl: String?,
        val primary: Boolean,
) : Parcelable
