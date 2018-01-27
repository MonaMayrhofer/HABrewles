package at.obyoxar.habrewles.events

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

    val events: MutableMap<String, EventSource> = HashMap()

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
}