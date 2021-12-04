package no.kanters.playlistmgr.routing

import io.ktor.locations.*

@Location("/")
object Playlists {
    @Location("/playlists/new")
    object New

    @Location("/playlists/create")
    class Create(val name: String? = null)

    @Location("/playlists/{name}/edit")
    data class Edit(val name: String)

    @Location("/playlists/{name}/edit")
    data class Append(val name: String, val query: String)
}
