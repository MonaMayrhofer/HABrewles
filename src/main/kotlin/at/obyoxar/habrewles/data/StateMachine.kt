package at.obyoxar.habrewles.data

import at.obyoxar.habrewles.events.Event
import at.obyoxar.habrewles.events.EventProvider
import mu.KotlinLogging
import java.util.concurrent.*
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger {  }
class StateMachine(states: List<State>, vararg initialStates: State) {

    val states: List<State> = states


    private var runSemaphore: Semaphore = Semaphore(0)

    val traversers: MutableList<StateMachineTraverser> = initialStates.map { StateMachineTraverser(this, it) }.toMutableList()

    fun start(blocking: Boolean){
        logger.info("Starting up")
        traversers.forEach {
            thread {
                it.start()
            }
        }
        logger.info(" - Started up")

        if(blocking)
            runSemaphore.acquire()
    }

    /**
     * May be called from every thread
     */
    fun stop(){
        logger.info("Shutting down")
        traversers.forEach { it.stop() }
        logger.info(" - Shut down")
        runSemaphore.release()
    }
}