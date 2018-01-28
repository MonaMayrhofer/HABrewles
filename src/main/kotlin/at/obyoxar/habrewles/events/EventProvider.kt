package at.obyoxar.habrewles.events

import com.sun.org.apache.xpath.internal.operations.Bool
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }
class EventProvider private constructor() {

    companion object {
        @Volatile private var INSTANCE: EventProvider? = null

        val instance: EventProvider
            get() {
                return INSTANCE ?: synchronized(this){
                    INSTANCE ?: EventProvider().also { INSTANCE = it }
                }
            }
    }

    private val events: MutableMap<String, EventSource> = HashMap()

    fun start(){
        events.forEach { t, u ->
            logger.info("Starting EventSource: $t")
            u.startListening()
        }
    }

    fun stop() {
        events.forEach { t, u ->
            logger.info("Shutting down EventSource: $t")
            u.stopListening()
        }
    }

    fun addHandler(string: String, hashCode: Int, function: (Event) -> Unit) {
        val parts = string.split("/", limit=2)
        events[parts[0]]?.addHandler(if(parts.size > 1) parts[1] else "",hashCode, function) ?: throw RuntimeException("EventSource ${parts[0]} could not be found")
    }

    fun registerEventSource(s: String, eventSource: EventSource) {
        events[s] = eventSource
    }

    fun exists(event: String) : Boolean {
        val parts = event.split("/", limit=2)
        if(parts.size != 2) return false
        return events[parts[0]]?.hasPath(parts[1]) ?: false
    }

    fun removeHandler(event: String, hashCode: Int) {
        val parts = event.split("/", limit=2)
        events[parts[0]]?.removeHandler(parts[1], hashCode) ?: throw RuntimeException("EventSource ${parts[0]} could not be found")
    }
}