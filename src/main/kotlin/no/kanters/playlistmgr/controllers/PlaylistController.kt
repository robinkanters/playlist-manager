package no.kanters.playlistmgr.controllers

import io.ktor.http.*
import io.ktor.locations.*
import kotlinx.html.*
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.submit
import kotlinx.html.InputType.text
import kotlinx.html.stream.createHTML
import no.kanters.playlistmgr.controllers.PlaylistEntry.*
import no.kanters.playlistmgr.logic.PlaylistUtils
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

object PlaylistView {
    fun renderFind(locations: Locations, playlistNames: List<String>) = createHTML().div {
        h2 { +"Playlist list" }
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
                playlistNames.map { pl ->
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

    fun renderNew(locations: Locations) = createHTML().div {
        form(method = post, action = locations.href(Playlists.Create)) {
            input(text, name = PlaylistController.parameterName)
            input(submit) { value = "Create!" }
        }
    }

    fun renderCreate(name: String?, locations: Locations): String {
        if (name == null) {
            return createHTML().span {
                classes = setOf("error")
                +"Couldn't create playlist"
            } + renderNew(locations)
        }

        return createHTML().div {
            h2 { +"Playlist created" }
            ul {
                li { a(locations.href(Playlists)) { +"< to list" } }
                li { a(locations.href(Playlists.Edit(name))) { +"edit >" } }
            }
        }
    }

    fun renderEdit(locations: Locations, playlist: Playlist) = createHTML().div {
        h2 { +playlist.name }
        a(locations.href(Playlists)) { +"< back" }
        ul {
            playlist.entries.forEach {
                li {
                    renderEntry(it)
                }
            }
        }
    }

    private fun HtmlBlockTag.renderEntry(pl: PlaylistEntry) {
        pl.comment?.let { span { classes = setOf("comment"); +it } }
        when (pl) {
            is YoutubeSearch -> +"YOUTUBE: ${pl.query}"
            is Shuffle -> +"<< Shuffle the playlist >>"
            is LineComment -> {
                classes = classes + "line-comment"
                +pl.comment
            }
            is UriLiteral -> +pl.url.toString()
        }
    }
}