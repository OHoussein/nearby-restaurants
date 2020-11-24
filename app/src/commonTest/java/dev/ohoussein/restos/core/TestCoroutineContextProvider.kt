package dev.ohoussein.restos.core

import dev.ohoussein.restos.common.coroutine.CoroutineContextProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestCoroutineContextProvider(dispatcher: CoroutineDispatcher? = null) : CoroutineContextProvider() {

    override val main = dispatcher ?: Dispatchers.Unconfined
    override val io = dispatcher ?: Dispatchers.Unconfined
}
