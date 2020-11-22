package dev.ohoussein.restos.ui.core.util

import dev.ohoussein.restos.ui.core.model.UiResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.uiResourceFlow(): Flow<UiResource<T>> {
    return this
            .map<T, UiResource<T>> {
                UiResource.Success(it)
            }
            .onStart {
                emit(UiResource.Loading<T>())
            }
            .catch {
                Timber.e(it)
                emit(UiResource.Error(it))
            }
}
