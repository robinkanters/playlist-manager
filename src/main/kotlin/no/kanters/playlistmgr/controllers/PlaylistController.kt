package no.kanters.playlistmgr.controllers

import io.ktor.http.*
import no.kanters.playlistmgr.logic.PlaylistUtils
import no.kanters.playlistmgr.models.Playlist
import no.kanters.playlistmgr.routing.Playlists

object PlaylistController {
    const val parameterName = "name"

    fun find() = PlaylistUtils.getPlaylists()

    fun create(parameters: Parameters): String? {
        val name = parameters[parameterName]
        if (name.isNullOrBlank())
            return null

        return PlaylistUtils.create(name)
    }

    fun edit(pl: Playlists.Edit) = Playlist(pl.name, PlaylistUtils.parse(PlaylistUtils.read(pl.name)))
}
