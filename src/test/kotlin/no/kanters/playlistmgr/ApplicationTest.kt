package no.kanters.playlistmgr

import io.ktor.html.*
import kotlinx.html.*
import io.ktor.routing.*
import io.ktor.http.*
import kotlinx.css.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.locations.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import no.kanters.playlistmgr.plugins.configureRouting
import no.kanters.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello World!", response.content)
            }
        }
    }
}