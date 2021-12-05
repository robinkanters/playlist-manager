package no.kanters.playlistmgr.plugins

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.css.*
import kotlinx.css.Color.Companion.white
import kotlinx.css.Display.block
import kotlinx.css.FontStyle.Companion.italic

private val stylesheet: CSSBuilder.() -> Unit = {
    body {
        backgroundColor = white
    }

    "span.comment" {
        display = block
        fontStyle = italic
        color = Color("#333")
    }

    "table.playlists tr > *" {
        border = "3px double black"
        padding(5.px)
    }
}

fun Application.configureTemplating() {
    routing {
        get("/styles.css") {
            call.respondCss(stylesheet)
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

