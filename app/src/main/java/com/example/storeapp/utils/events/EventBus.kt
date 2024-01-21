package com.example.storeapp.utils.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance

object EventBus {
    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> get() = _events.asSharedFlow()



    suspend fun publish(event: Event) {
        _events.emit(event)
    }


    suspend inline fun <reified T : Event> subscribe(
        scope: CoroutineScope,
        crossinline onEvent: (T) -> Unit
    ) {
        events
            .filterIsInstance<T>()
            .collectLatest {
                scope.coroutineContext.ensureActive()
                onEvent(it)
            }
    }
}
