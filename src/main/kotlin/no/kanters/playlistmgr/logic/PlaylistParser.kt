package no.kanters.playlistmgr.logic

import no.kanters.playlistmgr.models.PlaylistEntry
import no.kanters.playlistmgr.models.PlaylistEntry.*
import java.io.File
import java.net.URI
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

object PlaylistUtils {
    private val path = System.getenv("PLAYLISTS_PATH") ?: "/playlists"

    fun getPlaylists() =
        Path(path).listDirectoryEntries("*.txt").map { it.fileName.toString().dropLast(4) }

    fun create(name: String) = if (File(path, "${name}.txt").createNewFile()) name else null

    fun read(name: String) = Path(path).resolve("$name.txt").toFile().readLines()

    tailrec fun parse(
        lines: List<String>,
        comment: String? = null,
        entries: List<PlaylistEntry> = emptyList()
    ): List<PlaylistEntry> {
        val line = lines.firstOrNull()?.trim()
        return when {
            line == null -> entries
            line.isBlank() -> parse(lines.drop(1), comment, entries)

            line == "#shuffle" ->
                parse(lines.drop(1), null, entries + Shuffle)

            line startsWith "##" ->
                parse(
                    lines.drop(1), null, entries + LineComment(line.drop(2).trim())
                )

            line startsWith "#" ->
                parse(lines.drop(1), line.substringAfter('#').trim(), entries)

            line startsWith "ytsearch:" ->
                parse(
                    lines.drop(1), null, entries + YoutubeSearch(
                        line.substringAfter(':'), comment
                    )
                )

            line startsWith "http" ->
                parse(
                    lines.drop(1), null, entries + UriLiteral(URI.create(line), comment)
                )

            else -> throw Exception("Couldnt parse playlist line: '$line'")
        }
    }

    private infix fun String.startsWith(prefix: String) =
        regionMatches(0, prefix, 0, prefix.length, false)
}
