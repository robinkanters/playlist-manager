package no.kanters.playlistmgr.routing

import io.ktor.http.*
import io.ktor.locations.*
import no.kanters.playlistmgr.plugins.InvalidPayloadException

@Location("/")
object Playlists {
    @Location("/playlists/new")
    object New

    @Location("/playlists/create")
    object Create

    @Location("/playlists/{name}/edit")
    data class Edit(val name: String)

    @Location("/playlists/{name}/edit")
    data class Append(val name: String, val query: String)
}

@Location("/playlists/{name}/songs")
class Songs(val name: String) {
    @Location("/new")
    class New(val parent: Songs)

    @Location("/new")
    class Create(val parent: Songs) {
        companion object {
            const val paramName = "type"
            const val paramQuery = "query"
            const val paramComment = "comment"

            fun parsePayload(parameters: Parameters): Payload {
                return Payload(
                    parameters[paramName] ?: throw InvalidPayloadException,
                    parameters[paramQuery] ?: throw InvalidPayloadException,
                    parameters[paramComment]
                )
            }

            data class Payload(val type: String, val query: String, val comment: String?)
        }
    }
}