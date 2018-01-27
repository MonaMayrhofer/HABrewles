package at.obyoxar.habrewles.events

import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {  }
class TimerEventSource : EventSource() {

    private var timer: Timer? = null

    override fun stopListening() {
        logger.info("Stopping...")
        timer?.cancel()
    }

    override fun startListening() {
        logger.info("Starting...")
        timer = kotlin.concurrent.timer(initialDelay = 3000, period = 3000, action = {
            invoke(Event())
        })
    }
}