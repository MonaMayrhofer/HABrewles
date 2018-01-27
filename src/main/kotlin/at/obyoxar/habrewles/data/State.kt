package at.obyoxar.habrewles.data

import at.obyoxar.habrewles.events.Event
import mu.KotlinLogging


private val logger = KotlinLogging.logger {  }
class State (val name: String) {
    val futureStates: MutableList<Transition> = ArrayList()
    fun disengage(){
        futureStates.forEach {
            it.disengage()
        }
    }

    fun engage() {
        futureStates.forEach {
            it.engage()
        }
    }

    fun addTransition(state: State, event: String, handler: (Event) -> Boolean) {
        futureStates.add(Transition(this, state, event, handler))
    }

}
