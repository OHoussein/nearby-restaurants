package dev.ohoussein.restos.ui.feature.venues.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.ohoussein.restos.ui.core.model.UiVenue

class ShareRestaurantsViewModel : ViewModel() {

    val selectedVenue = MutableLiveData<UiVenue?>(null)
}