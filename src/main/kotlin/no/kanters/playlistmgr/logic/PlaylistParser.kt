package no.kanters.playlistmgr.logic

import no.kanters.playlistmgr.controllers.PlaylistEntry
import no.kanters.playlistmgr.controllers.PlaylistEntry.*
import java.net.URI

class PlaylistParser {
    tailrec fun parse(
        lines: List<String>,
        comment: String? = null,
        entries: List<PlaylistEntry> = emptyList()
    ): List<PlaylistEntry> {
        val line = lines.firstOrNull()?.trim()
        return when {
            line == null -> entries
            line.isBlank() -> parse(lines.drop(1), comment, entries)

            line == "#shuffle" -> parse(lines.drop(1), null, entries + Shuffle)
            line.startsWith("##") -> parse(
                lines.drop(1), null, entries + LineComment(line.drop(2).trim())
            )
            line.startsWith("#") -> parse(lines.drop(1), line.substringAfter('#').trim(), entries)
            line.startsWith("ytsearch:") -> parse(
                lines.drop(1), null, entries + YoutubeSearch(
                    line.substringAfter(':'), comment
                )
            )
            line.startsWith("http") -> parse(
                lines.drop(1), null, entries + UriLiteral(URI.create(line), comment)
            )

            else -> throw Exception("Couldnt parse playlist line: '$line'")
        }
    }
}