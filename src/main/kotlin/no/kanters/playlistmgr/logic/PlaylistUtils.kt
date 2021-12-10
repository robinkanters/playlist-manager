package no.kanters.playlistmgr.logic

import no.kanters.playlistmgr.models.PlaylistEntry
import no.kanters.playlistmgr.models.PlaylistEntry.*
import no.kanters.playlistmgr.routing.Songs
import java.io.File
import java.io.IOException
import java.net.URI
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

object PlaylistUtils {
    private val path = System.getenv("PLAYLISTS_PATH") ?: "/playlists"

    fun getPlaylists() =
        Path(path).listDirectoryEntries("*.txt").map { it.fileName.toString().dropLast(4) }

    fun create(name: String) = if (File(path, "${name}.txt").createNewFile()) name else null

    fun read(name: String) = getFile(name).readLines()

    private fun getFile(name: String) = Path(path).resolve("$name.txt").toFile()

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

    fun appendEntry(parent: Songs, entry: PlaylistEntry): Boolean {
        val commentLine = entry.comment?.orNullIfBlank()?.let { "# $it" } ?: ""
        val textContent = when (entry) {
            is YoutubeSearch -> {
                "$commentLine\nytsearch:${entry.query}"
            }
            is UriLiteral -> {
                "$commentLine\n${entry.url}"
            }
            is Shuffle -> {
                "$commentLine\n#shuffle"
            }
            is LineComment -> {
                "# $commentLine"
            }
        }

        return try {
            val f = getFile(parent.name)
            if (!f.readLines().lastOrNull().isNullOrBlank())
                f.appendText("\n")
            f.appendText("$textContent\n")
            true
        } catch (e: IOException) {
            false
        }
    }

    private fun String?.orNullIfBlank() = if (!isNullOrBlank()) this else null
}
