package no.kanters.playlistmgr.plugins

import freemarker.cache.ClassTemplateLoader
import freemarker.cache.TemplateLoader
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.css.*
import kotlinx.css.Color.Companion.paleVioletRed
import kotlinx.css.Color.Companion.red
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

    "span.error" {
        display = block
        backgroundColor = paleVioletRed
        borderColor = red
        borderStyle = BorderStyle.solid
        borderWidth = 1.px
    }

    "table.playlists tr > *" {
        border = "3px double black"
        padding(5.px)
    }
}

fun Application.configureTemplating() {
    install(FreeMarker) {
        class SuffixTemplateLoader(private val base: TemplateLoader): TemplateLoader by base {
            override fun findTemplateSource(name: String): Any?
                = base.findTemplateSource(if (name.endsWith(".ftl")) name else "$name.ftl")
        }

        localizedLookup = false
        templateLoader = SuffixTemplateLoader(ClassTemplateLoader(this::class.java.classLoader, "views"))
    }

    routing {
        get("/styles.css") {
            call.respondCss(stylesheet)
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

