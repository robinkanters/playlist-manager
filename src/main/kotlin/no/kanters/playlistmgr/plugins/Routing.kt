package no.kanters.playlistmgr.plugins

import io.ktor.http.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.ContentType.Text.Html
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import no.kanters.playlistmgr.controllers.PlaylistController
import no.kanters.playlistmgr.controllers.makeControllers
import no.kanters.playlistmgr.routing.Playlists
import kotlin.reflect.KClass


inline operator fun <reified T> Map<KClass<out Any>, (Any) -> String>.invoke(input: T) =
    (this[T::class] as? (T) -> String)?.invoke(input)
        ?: throw RoutingException("Controller inference failed for type ${T::class}")

fun Application.configureRouting() {
    install(Locations)
    install(AutoHeadResponse)

    val controllers: Map<KClass<out Any>, (Any) -> String> = makeControllers(this)
        .mapValues {
            { obj ->
                createHTML().html {
                    head {
                        link("/styles.css", "stylesheet", "text/css")
                    }
                    body {
                        unsafe { raw(it.value(obj)) }
                    }
                }
            }
        }

    if (System.getenv("HSTS")?.toBooleanStrictOrNull() == true) {
        install(HSTS)
    }

    routing {
        get<Playlists> {
            call.respondText(Html, OK) { controllers(it) }
        }
        get<Playlists.Edit> {
            call.respondText(Html, OK) { controllers(it) }
        }
        get<Playlists.New> {
            call.respondText(Html, OK) { controllers(it) }
        }
        post<Playlists.Create> {
            call.respondText(Html, OK) {
                controllers(Playlists.Create(call.receiveParameters()[PlaylistController.parameterName]))
            }
        }

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
                cause.printStackTrace()
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
                cause.printStackTrace()
            }
            exception<RoutingException> { cause ->
                call.respond(HttpStatusCode.NotFound)
                cause.printStackTrace()
            }
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
class RoutingException(msg: String) : RuntimeException(msg)
