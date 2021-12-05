package no.kanters.playlistmgr.controllers

import io.ktor.locations.*
import kotlinx.html.*
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.submit
import kotlinx.html.InputType.text
import kotlinx.html.stream.createHTML
import no.kanters.playlistmgr.log
import no.kanters.playlistmgr.routing.Songs

object PlaylistSongController {
    const val parameterName = "name"

    fun new(data: Songs.New, locations: Locations) = createHTML().div {
        h2 { +"New song in ${data.parent.name}" }
        form(method = post, action = locations.href(Songs.Create(data.parent))) {
            radioInput(name = "type") { +"URL" }
            br()
            radioInput(name = "type") { +"YouTube Query" }
            br()
            radioInput(name = "type") { +"Shuffle Playlist" }
            br()
            label {
                htmlFor = "name"
                +"Query"
            }
            br()
            input(text, name = "name")
            br()
            label {
                htmlFor = "comment"
                +"Comment (optional)"
            }
            br()
            input(text, name = "comment")
            br()
            input(submit) { value = "Create!" }
        }
    }

    fun create(data: Songs.Create, payload: Songs.Create.Companion.Payload): String {
        log.info(data.parent.toString())
        log.info(payload.toString())

        TODO()
    }
}
