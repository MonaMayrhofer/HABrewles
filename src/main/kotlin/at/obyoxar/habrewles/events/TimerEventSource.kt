package at.obyoxar.habrewles.events

import mu.KotlinLogging
import java.util.Timer
import kotlin.concurrent.schedule

private val logger = KotlinLogging.logger {  }
class TimerEventSource(val paths: Collection<Long>) : EventSource(paths.map { it.toString() }) {

    private val timer: Timer = Timer()

    override fun stopListening() {
        timer.cancel()
    }

    override fun startListening() {
        paths.forEach {
            timer.schedule(it*1000, it*1000, {
                invoke(it.toString(), Event())
            })
        }
    }

    //TODO When event registers @ t = 4, a 5 second delay only is 1s
}