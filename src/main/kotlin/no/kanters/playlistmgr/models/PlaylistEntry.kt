package no.kanters.playlistmgr.models

import java.net.URI

sealed class PlaylistEntry(open val comment: String? = null) {
    data class LineComment(override val comment: String) : PlaylistEntry(comment)
    data class UriLiteral(val url: URI, override val comment: String? = null) : PlaylistEntry(comment)
    data class YoutubeSearch(val query: String, override val comment: String? = null) : PlaylistEntry(comment)
    object Shuffle : PlaylistEntry(null)
}
