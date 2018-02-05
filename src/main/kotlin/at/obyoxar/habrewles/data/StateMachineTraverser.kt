package at.obyoxar.habrewles.data

import at.obyoxar.habrewles.events.Event
import at.obyoxar.habrewles.events.EventProvider
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.Semaphore

private val logger = KotlinLogging.logger {  }
class StateMachineTraverser(
        val stateMachine: StateMachine,
        initialState: State){

    private var currentState: State = initialState
        set(value) { //TODO Test this
            if (Thread.currentThread() == traverserThread){
                field = value
            }else{
                throw IllegalAccessException("CurrentState may only be altered from the main Thread")
            }
        }

    val traverserId = UUID.randomUUID()

    private var currentTransitionId: Long = Long.MIN_VALUE

    private var currentTransition: Transition? = null
    private val currentTransitionSemaphore = Semaphore(0)

    private var isTraversing = false

    private var traverserThread: Thread = Thread.currentThread()

    var running: Boolean
        set(value) {
            if(value != isTraversing){
                if(value)
                    start()
                else
                    stop()
            }
        }
        get() {
            return isTraversing
        }

    /**
     * Only the [traverserThread] may ever be anywhere in this method
     * Only one thread (the [traverserThreadb]) may ever wait for the [currentTransitionSemaphore]
     */
    fun start(){
        if(isTraversing) throw IllegalStateException("Traverser is already started!")
        isTraversing = true
        traverserThread = Thread.currentThread()

        logger.info("Traverser: ${stateMachine.stateMachineId} $traverserId Started up.")

        //Loop Start
        while(isTraversing){

            //Start State
            currentState.engage(this)

            //Wait
            currentTransitionSemaphore.acquire()

            //Stop State
            currentState.disengage(this)
            if(isTraversing)
                currentState = currentTransition!!.toState
        }
        //Loop End

        logger.info("Traverser: ${stateMachine.stateMachineId} $traverserId Shut down.")
        isTraversing = false
    }

    fun stop(){
        if(!isTraversing) throw IllegalStateException("Traverser is already stopped!")
        logger.info("Traverser: ${stateMachine.stateMachineId} $traverserId Shutting down.")
        isTraversing = false
        currentTransitionSemaphore.release()
    }

    /**
     * Called from every thread
     */
    private fun transitionTriggered(transition: Transition, expectedTransitionId: Long){
        synchronized(currentTransitionId) {
            if (expectedTransitionId == currentTransitionId) {
                currentTransitionId++
                currentTransition = transition
                currentTransitionSemaphore.release()
            }
        }
    }

    /**
     * Enables this transition to listen for one event
     * @param transition The Transition to be added to the Event
     */
    fun schedule(transition: Transition){
        logger.info("Traverser ${traverserId} waits for: ${transition.event}")
        val thisSchedulesTransitionIdPersistence = currentTransitionId
        EventProvider.instance.addHandler(transition.event, transition.hashCode(), {
            event ->
                if(transition.eventFilter(event))
                    transitionTriggered(transition, thisSchedulesTransitionIdPersistence)
        })
    }

    /**
     * Remove the transition from this event
     */
    fun unschedule(transition: Transition){
        EventProvider.instance.removeHandler(transition.event, transition.hashCode())
    }
}