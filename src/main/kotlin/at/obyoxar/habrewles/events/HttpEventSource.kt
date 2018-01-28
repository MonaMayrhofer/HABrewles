package at.obyoxar.habrewles.events

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.PathMethod
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer


class HttpEventSource(paths: List<String>) : EventSource(paths){

    val server : Http4kServer

    init {
        val app = routes(
            *Array(paths.size) {index ->
                paths[index] bind Method.GET to {_ ->
                    invoke(paths[index],Event())
                    Response(OK)
                }
            }
        )
        server = app.asServer(Jetty(8080))
    }

    override fun startListening() {
        server.start()
    }

    override fun stopListening() {
        server.stop()
    }
}