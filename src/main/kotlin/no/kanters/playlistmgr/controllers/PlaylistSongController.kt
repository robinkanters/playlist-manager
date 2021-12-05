package no.kanters.playlistmgr.controllers

import io.ktor.locations.*
import kotlinx.html.*
import kotlinx.html.FormMethod.post
import kotlinx.html.InputType.submit
import kotlinx.html.InputType.text
import no.kanters.playlistmgr.log
import no.kanters.playlistmgr.routing.Songs

object PlaylistSongController {
    const val parameterName = "name"

    data class NewPayload(val playlistName: String)

    fun create(data: Songs.Create, payload: Songs.Create.Companion.Payload): String {
        log.info(data.parent.toString())
        log.info(payload.toString())

        TODO()
    }
}

