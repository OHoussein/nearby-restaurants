package dev.ohoussein.restos.core

import dev.ohoussein.restos.common.coroutine.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers

class TestCoroutineContextProvider : CoroutineContextProvider() {

    override val main = Dispatchers.Unconfined
    override val io = Dispatchers.Unconfined
}