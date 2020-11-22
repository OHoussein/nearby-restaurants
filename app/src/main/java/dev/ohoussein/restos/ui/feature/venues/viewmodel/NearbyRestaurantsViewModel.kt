package dev.ohoussein.restos.ui.feature.venues.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.ohoussein.restos.common.coroutine.CoroutineContextProvider
import dev.ohoussein.restos.domain.model.GetVenuesParams
import dev.ohoussein.restos.domain.usecase.GetNearbyVenuesUseCase
import dev.ohoussein.restos.ui.core.mapper.UiDomainModelMapper
import dev.ohoussein.restos.ui.core.model.UiResource
import dev.ohoussein.restos.ui.core.model.UiVenue
import dev.ohoussein.restos.ui.core.model.UiViewPort
import dev.ohoussein.restos.ui.core.util.uiResourceFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class NearbyRestaurantsViewModel @ViewModelInject constructor(
        private val coroutineContextProvider: CoroutineContextProvider,
        private val useCase: GetNearbyVenuesUseCase,
        private val modelsMapper: UiDomainModelMapper) : ViewModel() {

    companion object {
        const val LIMIT_VENUE_LIST = 50
    }

    private val viewPort = MutableLiveData<UiViewPort>()

    val restaurantList: LiveData<UiResource<List<UiVenue>>> = viewPort.switchMap { uiViewPort ->
        //TODO read from cache first
        val params = GetVenuesParams(
                coordinates = modelsMapper.toDomain(uiViewPort.center),
                radius = uiViewPort.radius,
                limit = LIMIT_VENUE_LIST,
        )
        useCase.get(params)
                .map { list -> list.map { modelsMapper.toUiModel(it) } }
                .uiResourceFlow()
                .flowOn(coroutineContextProvider.io)
                .asLiveData()
    }

    fun updateViewPort(uiViewPort: UiViewPort) {
        viewPort.value = uiViewPort
    }
}
