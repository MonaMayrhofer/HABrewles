package at.obyoxar.habrewles.events

import mu.KotlinLogging
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.Lock

private val logger = KotlinLogging.logger {  }
abstract class EventSource(paths: Collection<String>) {
    private val handlers: MutableMap<String, MutableList<Pair<(Event) -> Unit, Int>>> = HashMap()


    init {
        paths.forEach { handlers[it] = ArrayList() }
    }

    operator fun invoke(path: String, event: Event){
        synchronized(handlers){
            handlers[path]?.forEach { it.first(event) } ?: throw RuntimeException("Path $path not found")
        }
    }

    abstract fun startListening()

    abstract fun stopListening()

    fun addHandler(path: String, hashCode: Int, function: (Event) -> Unit){
        synchronized(handlers) {
            handlers[path]?.add(function to hashCode) ?: throw RuntimeException("Path $path not found")
        }
    }

    fun hasPath(string: String): Boolean {
        return string in handlers.keys
    }

    fun removeHandler(path: String, hashCode: Int) {
        synchronized(handlers) {
            handlers[path]?.removeIf {
                it.second == hashCode
            } ?: throw RuntimeException("Path $path not found")
        }
    }
}