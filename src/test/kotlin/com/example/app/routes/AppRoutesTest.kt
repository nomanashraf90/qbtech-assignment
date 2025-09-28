package com.example.app.routes

import com.example.app.models.AppRequest
import com.example.app.models.AppResponse
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.ktor.serialization.jackson.jackson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class AppRoutesTest {

    private val mapper = jacksonObjectMapper()
        .registerKotlinModule()
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)

    @Test
    fun testBenfordApiSuccess() = testApplication {
        application {
            install(ContentNegotiation) { jackson() }
            routing { appRoutes() }
        }

        val request = AppRequest(
            text = "123, 1456, 1789, 1999, 2234, 2345, 2678, 2879, 2998",
            significanceLevel = 0.05
        )

        val jsonBody = mapper.writeValueAsString(request)

        val response: HttpResponse = client.post("/api/benford") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val body: AppResponse = mapper.readValue(response.bodyAsText(), AppResponse::class.java)
        assertTrue(body.followsBenford, "Following Benford’s law")
    }

    @Test
    fun testBenfordApiFailure() = testApplication {
        application {
            install(ContentNegotiation) { jackson() }
            routing { appRoutes() }
        }

        val request = AppRequest(
            text = "9000, 9500, 9900, 9800, 9700", // All start with 9, likely failing Benford
            significanceLevel = 0.05
        )

        val jsonBody = mapper.writeValueAsString(request)

        val response: HttpResponse = client.post("/api/benford") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val body: AppResponse = mapper.readValue(response.bodyAsText(), AppResponse::class.java)
        assertFalse(body.followsBenford, "Not following Benford’s law")
    }

    @Test
    fun testBenfordApiInvalidSignificanceLevel() = testApplication {
        application {
            install(ContentNegotiation) { jackson() }
            routing { appRoutes() }
        }

        val request = AppRequest(
            text = "123, 456, 789",
            significanceLevel = 1.5 // invalid
        )

        val jsonBody = mapper.writeValueAsString(request)

        val response: HttpResponse = client.post("/api/benford") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        val errorResponse = mapper.readTree(response.bodyAsText())
        assertTrue(errorResponse.has("error"))
        assertEquals("significanceLevel must be between 0 and 1 exclusive", errorResponse["error"].asText())
    }

    @Test
    fun testBenfordApiParsingException() = testApplication {
        application {
            install(ContentNegotiation) { jackson() }
            routing { appRoutes() }
        }

        val request = AppRequest(
            text = "abc, xyz, ???",  // invalid numbers
            significanceLevel = 0.05
        )

        val jsonBody = mapper.writeValueAsString(request)

        val response: HttpResponse = client.post("/api/benford") {
            contentType(ContentType.Application.Json)
            setBody(jsonBody)
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        val errorResponse = mapper.readTree(response.bodyAsText())
        assertTrue(errorResponse.has("error"))
        assertTrue(errorResponse["error"].asText().contains("No numbers"), "Expected parsing error")
    }

}
