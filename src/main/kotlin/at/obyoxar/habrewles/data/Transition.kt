package at.obyoxar.habrewles.data

import at.obyoxar.habrewles.events.Event

class Transition(val fromState: State, val toState: State, val event: String, val handler: (Event) -> Boolean) {



    fun disengage() {
        //TODO Move things from StateMachine to here
    }

    fun engage() {
        Repository.instance.stateMachine.schedule(this, event, handler)
    }
}