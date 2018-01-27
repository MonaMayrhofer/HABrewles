package at.obyoxar.habrewles.events

import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }
abstract class EventSource{
    private val handlers: MutableList<(Event) -> Unit> = ArrayList()

    operator fun invoke(event: Event){
        synchronized(handlers) {
            handlers.forEach { it(event) }
        }
    }

    abstract fun startListening()

    abstract fun stopListening()

    fun addHandler(handler: (Event) -> Unit){
        synchronized(handlers) {
            handlers.add(handler)
        }
    }

    operator fun plusAssign(handler: (Event) -> Unit){
        addHandler(handler)
    }
}