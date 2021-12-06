package no.kanters.playlistmgr.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.http.HttpHeaders.Location
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.Forbidden
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import no.kanters.playlistmgr.controllers.PlaylistController
import no.kanters.playlistmgr.controllers.PlaylistSongController
import no.kanters.playlistmgr.models.PlaylistEntry
import no.kanters.playlistmgr.routing.Playlists
import no.kanters.playlistmgr.routing.Songs
import java.net.URLEncoder
import java.nio.charset.Charset

fun Application.configureRouting() {
    install(Locations)
    install(AutoHeadResponse)

    val playlistController = PlaylistController
    val playlistSongController = PlaylistSongController

    routing {

        get<Playlists> {
            val playlists = playlistController.find()
            call.respondFreeMarker(
                "playlist_find",
                mapOf(
                    "playlists" to playlists,
                    "newLink" to call.locations.href(Playlists.New),
                    "link" to { pl: String -> call.locations.href(Playlists.Edit(pl)) }
                )
            )
        }
        get<Playlists.Edit> {
            val playlist = playlistController.edit(it)
            call.respondFreeMarker(
                "playlist_edit",
                mapOf(
                    "backLink" to call.locations.href(Playlists),
                    "newLink" to call.locations.href(Songs.New(Songs(playlist.name))),
                    "playlist" to playlist,
                    "getEntryType" to { entry: PlaylistEntry -> entry::class.java.simpleName },
                    "yturl" to { `in`: String -> "https://www.youtube.com/results?search_query=${`in`.urlencode()}" },
                )
            )
        }

        get<Playlists.New> {
            val error = call.sessions.get<UserSession>()?.error
            call.sessions.set(UserSession())
            call.respondFreeMarker(
                "playlist_new",
                mapOf(
                    "error" to error,
                    "backLink" to call.locations.href(Playlists),
                    "createLink" to call.locations.href(Playlists.Create),
                )
            )
        }

        post<Playlists.Create> {
            val createdPlaylist = playlistController.create(call.receiveParameters())
            if (createdPlaylist == null) {
                call.sessions.set(UserSession("Could not create playlist"))
                call.redirect(BadRequest, call.locations.href(Playlists.New))
            } else {
                call.redirect(Created, call.locations.href(Playlists.Edit(createdPlaylist)))
            }
        }

        get<Songs.New> {
            call.respondFreeMarker(
                "song_new",
                mapOf(
                    "input" to PlaylistSongController.NewPayload(it.parent.name),
                    "backLink" to call.locations.href(Playlists.Edit(it.parent.name)),
                )
            )
        }

        post<Songs.Create> {
            val data = Songs.Create.parsePayload(call.receiveParameters())
            if (playlistSongController.create(it, data)) {
                val playlistUrl = call.locations.href(Playlists.Edit(it.parent.name))
                call.respondFreeMarker(
                    "song_created",
                    mapOf(
                        "playlist" to it.parent.name,
                        "addmoreurl" to call.locations.href(Songs.New(it.parent)),
                        "playlisturl" to playlistUrl,
                    )
                )
            } else {
                log.info("Could not create playlist for ${it.parent.name}: $data")
                call.redirect(BadRequest, call.locations.href(Songs.New(it.parent)))
            }
        }

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(Unauthorized)
                cause.printStackTrace()
            }
            exception<AuthorizationException> { cause ->
                call.respond(Forbidden)
                cause.printStackTrace()
            }
            exception<RoutingException> { cause ->
                call.respond(NotFound)
                cause.printStackTrace()
            }
            exception<InvalidPayloadException> { cause ->
                call.respond(BadRequest)
                cause.printStackTrace()
            }
        }
    }
}

private fun String.urlencode() = URLEncoder.encode(this, Charset.defaultCharset())

private suspend fun ApplicationCall.respondFreeMarker(template: String, model: Any?) =
    respond(FreeMarkerContent(template, model))

private suspend fun ApplicationCall.redirect(statusCode: HttpStatusCode, uri: String) {
    response.headers.append(Location, uri)
    respond(statusCode)
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
class RoutingException(msg: String) : RuntimeException(msg)
object InvalidPayloadException : RuntimeException()
