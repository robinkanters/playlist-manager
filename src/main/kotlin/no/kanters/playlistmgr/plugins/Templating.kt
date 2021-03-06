package no.kanters.playlistmgr.plugins

import freemarker.cache.ClassTemplateLoader
import freemarker.cache.TemplateLoader
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.css.*
import kotlinx.css.BorderStyle.solid
import kotlinx.css.Color.Companion.red
import kotlinx.css.Color.Companion.white
import kotlinx.css.Display.block
import kotlinx.css.FontStyle.Companion.italic
import kotlinx.css.LinearDimension.Companion.auto
import kotlinx.css.Position.fixed
import kotlinx.css.properties.lh

private val stylesheet: CSSBuilder.() -> Unit = {
    html {
        width = 100.pct
    }
    body {
        backgroundColor = white
        width = 100.pct
    }

    "body>#content" {
        paddingTop = 15.px
        margin(horizontal = auto)
        maxWidth = 1200.px
    }

    "span.comment" {
        display = block
        fontStyle = italic
        color = Color("#333")
    }

    "span.error" {
        display = block
        backgroundColor = Color("#FFCCCC")
        borderColor = red
        borderStyle = solid
        borderWidth = 1.px
        marginTop = 15.px
        marginBottom = 15.px
        padding(10.px)
    }

    "ul.playlist-entries li" {
        margin(3.px, null)
    }

    "body>div.footer" {
        position = fixed
        bottom = 0.px
        left = 0.px
        height = 64.px
        lineHeight = 64.px.lh
        width = 100.pct
        color = hex(0x333333)
        backgroundColor = hex(0xCCCCCC)
    }

    "body>div.footer .version" {
        margin(horizontal = 16.px)
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

