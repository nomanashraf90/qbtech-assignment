package com.example.app.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class AppRequestTest {

    private val json = Json { prettyPrint = true }

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
    fun testEmptyText() {
        val request = AppRequest(text = "")
        assertEquals("", request.text)
        assertEquals(0.05, request.significanceLevel)
    }

}
