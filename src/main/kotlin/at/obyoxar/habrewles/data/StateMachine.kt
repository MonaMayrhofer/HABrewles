package at.obyoxar.habrewles.data

import at.obyoxar.habrewles.events.Event
import at.obyoxar.habrewles.events.EventProvider
import mu.KotlinLogging
import java.util.concurrent.*
import kotlin.concurrent.thread

private val logger = KotlinLogging.logger {  }
class StateMachine(states: List<State>, initialState: State) {

    val states: List<State> = states
    var currentState: State = initialState
        private set

    private var transitionIdentificator: Int = 0 //TODO Can we omit this and work via currentState and FromState

    private var waitSemaphore: Semaphore = Semaphore(0)
    private var triggerSemaphore: Semaphore = Semaphore(0)


    fun start(){
        for (i in 0..3) { //TODO Increase this for production
            logger.info("Engaging State '${currentState.name}'")

            triggerSemaphore.drainPermits()
            thread {
                while (!waitSemaphore.hasQueuedThreads());
                triggerSemaphore.release()
            }
            this.currentState.engage()
            waitSemaphore.acquire()
        }
        logger.info("Shut down.")
    }

    fun transitionTriggered(transition: Transition){
        transitionIdentificator++

        //Release all (They will run into the transitionIdentificator Lock
        currentState.disengage()
        currentState = transition.toState
        waitSemaphore.release()
    }

    fun schedule(transition: Transition, string: String ,run: (Event) -> Boolean){
        logger.info("Registering transition on $string")
        val currentIdentificator = this.transitionIdentificator

        EventProvider.instance.events[string]?.addHandler {
            synchronized(transitionIdentificator){
                if(transitionIdentificator == currentIdentificator) {
                    if (run(it)) {
                        transitionTriggered(transition)
                    }else {
                        triggerSemaphore.release()
                    }
                }
            }
        } ?: throw RuntimeException("EventSource $string was not found")
    }
}