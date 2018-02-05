package at.obyoxar.habrewles.events

import mu.KotlinLogging

//TODO Add State tracker to avoid double start
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

    /**
     * Calls the [EventSource.startListening] method of every registered EventSource
     */
    fun start(){
        events.forEach { t, u ->
            logger.info("Starting EventSource: $t")
            u.startListening()
        }
    }

    /**
     * Calls the [EventSource.stopListening] method of every registered EventSource
     */
    fun stop() {
        events.forEach { t, u ->
            logger.info("Shutting down EventSource: $t")
            u.stopListening()
        }
    }

    /**
     * @param eventPath The path of the event to register on. Form [event/subpath].
     * @param hashCode The identifier for this subscription, so that the handler can be removed safely later. It should
     * be unique per [eventPath]
     * @param handler The eventhandler to be added
     */
    fun addHandler(eventPath: String, hashCode: Int, handler: (Event) -> Unit) {
        val parts = eventPath.split("/", limit=2)
        events[parts[0]]?.addHandler(if(parts.size > 1) parts[1] else "",hashCode, handler) ?: throw RuntimeException("EventSource ${parts[0]} could not be found")
    }

    /**
     * Register a new eventSource under event. Subpaths will be accessible under [event/subpath]
     * @param event The root path
     * @param eventSource The eventsource to register
     */
    fun registerEventSource(event: String, eventSource: EventSource) {
        events[event] = eventSource
    }

    /**
     * @param event The path in the form: [event/subpath]
     * @return If the given event and subpath exist
     */
    fun exists(event: String) : Boolean {
        val parts = event.split("/", limit=2)
        if(parts.size != 2) return false
        return events[parts[0]]?.hasPath(parts[1]) ?: false
    }

    /**
     * @param eventPath The path of the event to unregister from. Form [event/subpath]
     * @param hashCode The identifier for the subscription to remove. Has to be the same as in addHandler
     */
    fun removeHandler(eventPath: String, hashCode: Int) {
        val parts = eventPath.split("/", limit=2)
        events[parts[0]]?.removeHandler(parts[1], hashCode) ?: throw RuntimeException("EventSource ${parts[0]} could not be found")
    }
}