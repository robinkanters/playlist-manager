package no.kanters.playlistmgr.controllers

import io.ktor.locations.*
import kotlinx.html.*
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.submit
import kotlinx.html.InputType.text
import kotlinx.html.stream.createHTML
import no.kanters.playlistmgr.logic.PlaylistParser
import no.kanters.playlistmgr.routing.Playlists
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.listDirectoryEntries

class PlaylistController(
    private val locations: Locations,
    private val playlistParser: PlaylistParser,
    private val path: String
) {
    init {
        LoggerFactory.getLogger(PlaylistController::class.java.simpleName)
            .info("Using playlist files from $path")
    }

    private val playlists: List<String>
        get() = Path(path).listDirectoryEntries("*.txt").map { it.fileName.toString().dropLast(4) }

    private fun playlist(name: String): List<String> {
        val file = Path(path).resolve("$name.txt").toFile()
        return file.readLines()
    }

    companion object {
        const val parameterName = "name"
    }

    fun new() = createHTML().div {
        form(method = post, action = locations.href(Playlists.Create())) {
            input(text, name = "name")
            input(submit) { value = "Create!" }
        }
    }

    private fun getFile(name: String) = File(path, "${name}.txt")
    fun create(name: Playlists.Create): String {
        return if (!name.name.isNullOrBlank() && getFile(name.name).createNewFile()) {
            createHTML().div {
                h2 { +"Playlist created" }
                ul {
                    li { a(locations.href(Playlists)) { +"< to list" } }
                    li { a(locations.href(Playlists.Edit(name.name))) { +"edit >" } }
                }
            }
        } else {
            createHTML().span {
                classes = setOf("error")
                + "Couldn't create playlist"
            } + new()
        }
    }

    fun find() = createHTML().div {
        h2 {
            +"Playlist list"
        }
        a(locations.href(Playlists.New)) { +"New" }
        table {
            classes = setOf("playlists")
            thead {
                tr {
                    th { +"Name" }
                    th { +"Actions" }
                }
            }
            tbody {
                playlists.map { pl ->
                    tr {
                        td { +pl }
                        td {
                            a(locations.href(Playlists.Edit(pl))) { +"Edit" }
                        }
                    }
                }
            }
        }
    }

    fun edit(pl: Playlists.Edit) = createHTML().div {
        val playlist = playlistParser.parse(playlist(pl.name))

        h2 { +pl.name }
        a(locations.href(Playlists)) { +"< back" }
        ul {
            playlist.forEach {
                li {
                    it.comment?.let { span { classes = setOf("comment"); +it } }
                    when (it) {
                        is PlaylistEntry.YoutubeSearch -> +"YOUTUBE: ${it.query}"
                        is PlaylistEntry.Shuffle -> +"<< Shuffle the playlist >>"
                        is PlaylistEntry.LineComment -> {
                            classes = classes + "line-comment"
                            +it.comment
                        }
                        is PlaylistEntry.UriLiteral -> +it.url.toString()
                    }
                }
            }
        }
    }

    fun append(entry: Playlists.Append) = createHTML().div {
        val playlist = playlistParser.parse(playlist(entry.name))

    }
}
