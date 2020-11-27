package dev.ohoussein.restos.core

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

fun <T> delayedFlowOf(value: T, timeOutMillis: Long) = flow {
    delay(timeOutMillis)
    emit(value)
}
