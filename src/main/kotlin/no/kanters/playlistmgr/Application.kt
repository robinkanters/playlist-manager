package no.kanters.playlistmgr

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.kanters.playlistmgr.plugins.configureHTTP
import no.kanters.playlistmgr.plugins.configureMonitoring
import no.kanters.playlistmgr.plugins.configureRouting
import no.kanters.playlistmgr.plugins.configureTemplating

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureTemplating()
        configureRouting()
        configureMonitoring()
        configureHTTP()
    }.start(wait = true)
}
