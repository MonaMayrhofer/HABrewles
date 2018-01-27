package at.obyoxar.habrewles.events

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer


class HttpEventSource : EventSource(){

    var server : Http4kServer? = null

    override fun startListening() {
        val app = routes(
                "/ping" bind Method.GET to {
                    invoke(Event())
                    Response(OK)
                }
        )
        server = app.asServer(Jetty(8080)).start()
    }

    override fun stopListening() {
        server?.stop() ?: throw IllegalStateException("Cannot stop not-running Server")
    }

}