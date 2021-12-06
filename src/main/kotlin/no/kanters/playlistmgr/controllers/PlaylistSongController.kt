package no.kanters.playlistmgr.controllers

import no.kanters.playlistmgr.logic.PlaylistUtils
import no.kanters.playlistmgr.models.PlaylistEntry
import no.kanters.playlistmgr.plugins.InvalidPayloadException
import no.kanters.playlistmgr.routing.Songs
import java.net.URI

object PlaylistSongController {
    const val parameterName = "name"

    data class NewPayload(val playlistName: String)

    fun create(data: Songs.Create, payload: Songs.Create.Companion.Payload): Boolean {
        val entry = when (payload.type) {
            "url" -> PlaylistEntry.UriLiteral(URI.create(payload.query), payload.comment)
            "ytsearch" -> PlaylistEntry.YoutubeSearch(payload.query, payload.comment)
            "shuffle" -> PlaylistEntry.Shuffle
            else -> throw InvalidPayloadException
        }

        return PlaylistUtils.appendEntry(data.parent, entry)
    }
}

