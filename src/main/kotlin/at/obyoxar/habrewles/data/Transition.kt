package at.obyoxar.habrewles.data

import at.obyoxar.habrewles.events.Event
import at.obyoxar.habrewles.events.EventProvider
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }
class Transition(val fromState: State, val toState: State, val event: String, val handler: (Event) -> Boolean) {

    init {
        //Check if event is valid
        if(!EventProvider.instance.exists(event)) throw IllegalArgumentException("Path $event does not exist")
    }

    fun disengage() {
        logger.info("Disengaging transition")
        Repository.instance.stateMachine.unschedule(this, event, handler)
    }

    fun engage() {
        Repository.instance.stateMachine.schedule(this, event, handler)
    }

    override fun hashCode(): Int {
        var result = fromState.hashCode()
        result = 31 * result + toState.hashCode()
        result = 31 * result + event.hashCode()
        result = 31 * result + handler.hashCode()
        return result
    }
}