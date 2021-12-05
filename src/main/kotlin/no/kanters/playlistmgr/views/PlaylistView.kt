package no.kanters.playlistmgr.views

import io.ktor.locations.*
import kotlinx.html.*
import kotlinx.html.stream.createHTML
import no.kanters.playlistmgr.controllers.PlaylistController
import no.kanters.playlistmgr.models.Playlist
import no.kanters.playlistmgr.models.PlaylistEntry
import no.kanters.playlistmgr.routing.Playlists

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
        h2 { +"Create a playlist" }
        form(method = FormMethod.post, action = locations.href(Playlists.Create)) {
            label {
                htmlFor = PlaylistController.parameterName
                +"Name:"
            }
            br()
            input(InputType.text, name = PlaylistController.parameterName)
            br()
            input(InputType.submit) { value = "Create!" }
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
            is PlaylistEntry.YoutubeSearch -> +"YOUTUBE: ${pl.query}"
            is PlaylistEntry.Shuffle -> +"<< Shuffle the playlist >>"
            is PlaylistEntry.LineComment -> {
                classes = classes + "line-comment"
                +pl.comment
            }
            is PlaylistEntry.UriLiteral -> +pl.url.toString()
        }
    }
}