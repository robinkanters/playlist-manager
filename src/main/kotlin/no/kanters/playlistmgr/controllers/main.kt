package no.kanters.playlistmgr.controllers

import java.net.URI

data class Playlist(val name: String, val entries: List<PlaylistEntry>)

sealed class PlaylistEntry(open val comment: String?) {
    data class LineComment(override val comment: String) : PlaylistEntry(comment)
    data class UriLiteral(val url: URI, override val comment: String?) : PlaylistEntry(comment)
    data class YoutubeSearch(val query: String, override val comment: String?) : PlaylistEntry(comment)
    object Shuffle : PlaylistEntry(null)
}