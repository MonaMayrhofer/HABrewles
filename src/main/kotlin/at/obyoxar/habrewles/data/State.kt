package at.obyoxar.habrewles.data

import at.obyoxar.habrewles.events.Event
import mu.KotlinLogging


private val logger = KotlinLogging.logger {  }
class State (val name: String) {

    private val futureStates: MutableList<Transition> = ArrayList()

    fun disengage(){
        logger.info("Disengaging State '$name'")
        futureStates.forEach {
            it.disengage()
        }
    }

    fun engage() {
        logger.info("Engaging State '$name'")
        futureStates.forEach {
            it.engage()
        }
    }

    fun addTransition(state: State, event: String, accept: (Event) -> Boolean) {
        futureStates.add(Transition(this, state, event, accept))
    }
}
