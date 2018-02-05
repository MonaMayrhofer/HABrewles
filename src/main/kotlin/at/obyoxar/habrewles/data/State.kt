package at.obyoxar.habrewles.data

import at.obyoxar.habrewles.events.Event
import mu.KotlinLogging


private val logger = KotlinLogging.logger {  }
open class State (val name: String) {

    private val transitions: MutableList<Transition> = ArrayList()
    private var engaged = false

    fun disengage(stateMachineTraverser: StateMachineTraverser) {
        if(!engaged) throw IllegalStateException("State is already disengaged")
        engaged = false
        logger.info("Traverser ${stateMachineTraverser.traverserId} Disengaging State '$name'")
        transitions.forEach {
            stateMachineTraverser.unschedule(it)
        }
        disable(stateMachineTraverser)
    }

    protected open fun disable(stateMachineTraverser: StateMachineTraverser) = Unit
    protected open fun enable(stateMachineTraverser: StateMachineTraverser) = Unit

    fun engage(stateMachineTraverser: StateMachineTraverser) {
        if(engaged) throw IllegalStateException("State is already engaged")
        engaged = true
        logger.info("Traverser ${stateMachineTraverser.traverserId} Engaging State '$name'")

        enable(stateMachineTraverser)
        transitions.forEach {
            stateMachineTraverser.schedule(it)
        }
    }

    fun addTransition(state: State, event: String, accept: (Event) -> Boolean) {
        transitions.add(Transition(this, state, event, accept))
    }
}
