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

        EventProvider.instance.registerEventSource("Timer",TimerEventSource(listOf(1,5,10)))
        EventProvider.instance.registerEventSource("WebServer",HttpEventSource(listOf("toOne","toTwo")))

        var states = Array(3) {
            State("State: $it")
        }.toList()

        states[0].addTransition(states[1], "Timer/10", {true})
        states[0].addTransition(states[1], "WebServer/toOne", {true})
        states[1].addTransition(states[2], "WebServer/toTwo", {true})
        states[2].addTransition(states[0], "Timer/1", {true})

        Repository.initialize(StateMachine(states, states[0]))
    }

}