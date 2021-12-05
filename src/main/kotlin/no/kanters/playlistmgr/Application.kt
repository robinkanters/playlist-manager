package no.kanters.playlistmgr

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import no.kanters.playlistmgr.plugins.configureHTTP
import no.kanters.playlistmgr.plugins.configureMonitoring
import no.kanters.playlistmgr.plugins.configureRouting
import no.kanters.playlistmgr.plugins.configureTemplating
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

val loggerCache = mutableMapOf<KClass<*>, Logger>()

@Suppress("unused") // It is actually used but Kotlin Plugin doesn't realize it.
inline val <reified T> T.log
    get() = loggerCache.computeIfAbsent(T::class) { LoggerFactory.getLogger(T::class.java.simpleName) }

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureTemplating()
        configureRouting()
        configureMonitoring()
        configureHTTP()
    }.start(wait = true)
}
