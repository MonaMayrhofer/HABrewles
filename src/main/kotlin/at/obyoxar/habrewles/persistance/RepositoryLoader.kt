package at.obyoxar.habrewles.persistance

import at.obyoxar.habrewles.data.*
import at.obyoxar.habrewles.events.EventProvider
import at.obyoxar.habrewles.events.HttpEventSource
import at.obyoxar.habrewles.events.MqttEventSource
import at.obyoxar.habrewles.events.TimerEventSource
import mu.KotlinLogging

private val logger = KotlinLogging.logger {  }
class RepositoryLoader {

    fun load(){
        logger.info("Loading Repository")

        EventProvider.instance.registerEventSource("Timer",TimerEventSource(listOf(1,5,10)))
        EventProvider.instance.registerEventSource("WebServer",HttpEventSource(8080, listOf("toOne","toTwo","shutdown")))
        EventProvider.instance.registerEventSource("MQTT",MqttEventSource(
                "tcp://192.168.170.100:1883",
                "habrewles",
                listOf("openhab/out/MQTTest/state")))

        val states = listOf(
                State("5SecondWeb"),
                State("MqttWeb"),
                State("OneSecond"),
                State("Running"),
                LambdaStateWrapper("Shutdown", {
                    it.stateMachine.stop()
                }))

        states[0].addTransition(states[1], "Timer/5", {true})
        states[0].addTransition(states[1], "WebServer/toOne", {true})
        states[1].addTransition(states[2], "WebServer/toTwo", {true})
        states[1].addTransition(states[2], "MQTT/openhab/out/MQTTest/state", {true})
        states[2].addTransition(states[0], "Timer/1", {true})

        states[3].addTransition(states[4], "WebServer/shutdown",{true})

        Repository.initialize(StateMachine(states, states[0], states[3]))
    }

}