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
import dev.ohoussein.restos.ui.core.util.MapUtils.containedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import timber.log.Timber

class NearbyRestaurantsViewModel @ViewModelInject constructor(
        private val coroutineContextProvider: CoroutineContextProvider,
        private val useCase: GetNearbyVenuesUseCase,
        private val modelsMapper: UiDomainModelMapper) : ViewModel() {

    companion object {
        const val LIMIT_VENUE_LIST = 50
        var debounceTimer = 500L
    }

    private val viewPortLive = MutableLiveData<UiViewPort>()

    private val cachedVenueList = mutableListOf<UiVenue>()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val restaurantList: LiveData<UiResource<List<UiVenue>>> = viewPortLive
            .asFlow()
            .run {
                //TODO this is workoround because TestCoroutineScope  doesn't block the debounce
                if (debounceTimer > 0)
                    return@run debounce(debounceTimer)
                return@run this
            }
            .flatMapLatest { currentViewPort ->
                Timber.d("Call ws for $currentViewPort")
                val params = GetVenuesParams(
                        coordinates = modelsMapper.toDomain(currentViewPort.center),
                        radius = currentViewPort.radius,
                        limit = LIMIT_VENUE_LIST,
                )
                useCase.get(params)
                        .map { list -> list.map { modelsMapper.toUiModel(it) } }
                        .map {
                            addToCache(it)
                            it
                        }
                        .map<List<UiVenue>, UiResource<List<UiVenue>>> {
                            UiResource.Success(it)
                        }
                        .flowOn(coroutineContextProvider.io)
            }
            .onStart {
                val cached = viewPortLive.value?.let {
                    getFromCache(it)
                } ?: emptyList()
                emit(UiResource.Loading(cached))
            }
            .catch { error ->
                viewPortLive.value?.let {
                    val cached = getFromCache(it)
                    emit(UiResource.Error(error, cached))
                }
                Timber.e(error)
            }
            .onEach {
                if (it is UiResource.Success)
                    addToCache(it.data)
            }
            .flowOn(coroutineContextProvider.io)
            .asLiveData()

    private fun addToCache(list: List<UiVenue>) {
        //remove duplicate
        cachedVenueList.removeAll { cachedVenue ->
            list.any { it.id == cachedVenue.id }
        }
        cachedVenueList.addAll(list)
    }

    private fun getFromCache(viewPort: UiViewPort): List<UiVenue> {
        val filtred = cachedVenueList.filter {
            it.location.coordinates.containedIn(viewPort)
        }
        Timber.d("Filtred ${filtred.size} existed ${cachedVenueList.size}")
        return filtred
    }

    fun updateViewPort(uiViewPort: UiViewPort) {
        Timber.d("UpdateViewPort $uiViewPort")
        viewPortLive.value = uiViewPort
    }
}
