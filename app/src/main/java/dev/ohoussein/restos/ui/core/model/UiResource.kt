package dev.ohoussein.restos.ui.core.model


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */

sealed class UiResource<out T> {

    data class Success<T>(val data: T) : UiResource<T>()
    data class Error<T>(val error: Throwable, val data: T? = null) : UiResource<T>()
    data class Loading<T>(val data: T? = null) : UiResource<T>()
}
