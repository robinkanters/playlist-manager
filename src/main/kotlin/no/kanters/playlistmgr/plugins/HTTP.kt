package no.kanters.playlistmgr.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.sessions.*

data class UserSession(val error: String? = null)

fun Application.configureHTTP() {
    install(Sessions) {
        cookie<UserSession>("session", SessionStorageMemory())
    }
    install(DefaultHeaders) {
        header("X-Engine", "Ktor") // will send this header with each response
    }
    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024)
        }
    }
}
