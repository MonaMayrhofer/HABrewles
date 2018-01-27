package at.obyoxar.habrewles.persistance

import at.obyoxar.habrewles.data.*
import at.obyoxar.habrewles.events.EventProvider
import at.obyoxar.habrewles.events.HttpEventSource
import at.obyoxar.habrewles.events.TimerEventSource
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }
class RepositoryLoader {

    fun load(){
        logger.info("Loading Repository")

        EventProvider.instance.events["3SecTimer"] = TimerEventSource()
        EventProvider.instance.events["WebServer"] = HttpEventSource()

        var states = Array(3) {
            State("State: $it")
        }.toList()

        states[0].addTransition(states[1], "3SecTimer", {true})
        states[0].addTransition(states[2], "WebServer", {true})
        states[1].addTransition(states[2], "3SecTimer", {true})
        states[2].addTransition(states[0], "3SecTimer", {true})

        Repository.initialize(StateMachine(states, states[0]))
    }

}