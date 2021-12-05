package no.kanters.playlistmgr.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.http.ContentType.Text.Html
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import no.kanters.playlistmgr.controllers.PlaylistController
import no.kanters.playlistmgr.controllers.PlaylistSongController
import no.kanters.playlistmgr.routing.Playlists
import no.kanters.playlistmgr.routing.Songs
import no.kanters.playlistmgr.views.PlaylistView

fun Application.configureRouting() {
    install(Locations)
    install(AutoHeadResponse)

    if (System.getenv("HSTS")?.toBooleanStrictOrNull() == true) {
        install(HSTS)
    }

    val playlistController = PlaylistController
    val playlistSongController = PlaylistSongController

    routing {
        get<Playlists> {
            call.respondText(Html, OK) {
                val playlists = playlistController.find()
                PlaylistView.renderFind(call.locations, playlists)
            }
        }
        get<Playlists.Edit> {
            call.respondText(Html, OK) {
                val pl = playlistController.edit(it)
                PlaylistView.renderEdit(call.locations, pl)
            }
        }
        get<Playlists.New> {
            call.respondText(Html, OK) {
                PlaylistView.renderNew(call.locations)
            }
        }
        post<Playlists.Create> {
            call.respondText(Html, OK) {
                val createdPlaylist = playlistController.create(call.receiveParameters())
                PlaylistView.renderCreate(createdPlaylist, call.locations)
            }
        }

        suspend fun ApplicationCall.respondFreeMarker(template: String, model: Any?) = respond(FreeMarkerContent(template, model))

        get<Songs.New> {
            call.respondFreeMarker(
                "song_new",
                PlaylistSongController.NewPayload(it.parent.name)
            )
        }

        post<Songs.Create> {
            call.respondText(Html, OK) {
                val data = Songs.Create.parsePayload(call.receiveParameters())
                playlistSongController.create(it, data)
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
            exception<InvalidPayloadException> { cause ->
                call.respond(HttpStatusCode.BadRequest)
                cause.printStackTrace()
            }
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
class RoutingException(msg: String) : RuntimeException(msg)
object InvalidPayloadException : RuntimeException()