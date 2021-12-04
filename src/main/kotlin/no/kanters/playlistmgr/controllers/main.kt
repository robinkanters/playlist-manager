package no.kanters.playlistmgr.controllers

import io.ktor.application.*
import io.ktor.locations.*
import no.kanters.playlistmgr.logic.PlaylistParser
import no.kanters.playlistmgr.routing.Playlists
import java.net.URI
import kotlin.reflect.KClass

fun makeControllers(app: Application): Map<KClass<out Any>, (Any) -> String> {
    val playlistController = PlaylistController(app.locations, PlaylistParser(), System.getenv("PLAYLISTS_PATH") ?: "/playlists")

    return mapOf(
        Playlists::class to { playlistController.find() },
        Playlists.Edit::class to { pl -> playlistController.edit(pl as Playlists.Edit) },
        Playlists.New::class to { playlistController.new() },
        Playlists.Create::class to { pl -> playlistController.create(pl as Playlists.Create) },
    )
}

sealed class PlaylistEntry(open val comment: String?) {
    data class LineComment(override val comment: String): PlaylistEntry(comment)
    data class UriLiteral(val url: URI, override val comment: String?): PlaylistEntry(comment)
    data class YoutubeSearch(val query: String, override val comment: String?): PlaylistEntry(comment)
    object Shuffle: PlaylistEntry(null)
}