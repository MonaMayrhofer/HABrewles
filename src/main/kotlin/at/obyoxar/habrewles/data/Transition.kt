package at.obyoxar.habrewles.data

import at.obyoxar.habrewles.events.Event
import at.obyoxar.habrewles.events.EventProvider

data class Transition(
        val fromState: State,
        val toState: State,
        val event: String,
        val eventFilter: (Event) -> Boolean) {

    init {
        if(!EventProvider.instance.exists(event)) throw IllegalArgumentException("Path $event does not exist")
    }

    override fun hashCode(): Int {
        var result = fromState.hashCode()
        result = 31 * result + toState.hashCode()
        result = 31 * result + event.hashCode()
        result = 31 * result + eventFilter.hashCode()
        return result
    }
}