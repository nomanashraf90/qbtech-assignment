package com.example.app.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlinx.serialization.encodeToString

class AppRequestTest {

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true   // 👈 force default values into JSON
    }


    @Test
    fun testDefaultSignificanceLevel() {
        val request = AppRequest(text = "123, 456, 789")
        assertEquals("123, 456, 789", request.text)
        assertEquals(0.05, request.significanceLevel)
    }

    @Test
    fun testCustomSignificanceLevel() {
        val request = AppRequest(text = "100, 200", significanceLevel = 0.1)
        assertEquals("100, 200", request.text)
        assertEquals(0.1, request.significanceLevel)
    }

    @Test
    fun testSerialization() {
        val request: AppRequest = AppRequest(text = "123,456,789")
        val jsonString = json.encodeToString(request)
        val decoded = json.decodeFromString<AppRequest>(jsonString)
        assertEquals("123,456,789", decoded.text)
        assertEquals(0.05, decoded.significanceLevel)
    }

    @Test
    fun testDeserialization() {
        val jsonString = """
            {
                "text": "1000,2000,3000",
                "significanceLevel": 0.1
            }
        """.trimIndent()

        val request = json.decodeFromString<AppRequest>(jsonString)
        assertEquals("1000,2000,3000", request.text)
        assertEquals(0.1, request.significanceLevel, 1e-6)
    }

    @Test
    fun testEmptyText() {
        val request = AppRequest(text = "")
        assertEquals("", request.text)
        assertEquals(0.05, request.significanceLevel)
    }

}
