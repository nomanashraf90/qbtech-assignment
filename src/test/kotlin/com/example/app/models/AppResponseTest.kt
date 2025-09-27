package com.example.app.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlinx.serialization.encodeToString

class AppResponseTest {

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true   // 👈 force default values into JSON
    }

    @Test
    fun testInitialization() {
        val response = AppResponse(
            chiSquareValue = 3.84,
            pValue = 0.05,
            significanceLevel = 0.05,
            followsBenford = true,
            observedCounts = listOf(100L, 50L, 30L),
            expectedCounts = listOf(95.0, 55.0, 30.0)
        )

        assertEquals(3.84, response.chiSquareValue)
        assertEquals(0.05, response.pValue)
        assertEquals(0.05, response.significanceLevel)
        assertTrue(response.followsBenford)
        assertEquals(listOf(100L, 50L, 30L), response.observedCounts)
        assertEquals(listOf(95.0, 55.0, 30.0), response.expectedCounts)
    }

    @Test
    fun testSerialization() {
        val response: AppResponse = AppResponse(
            chiSquareValue = 10.5,
            pValue = 0.02,
            significanceLevel = 0.05,
            followsBenford = false,
            observedCounts = listOf(0L, 100L),
            expectedCounts = listOf(50.0, 50.0)
        )

        val jsonString = json.encodeToString(response)
        val decoded = json.decodeFromString<AppResponse>(jsonString)
        assertEquals(10.5,decoded.chiSquareValue)
        assertEquals(0.02,decoded.pValue)
    }

    @Test
    fun testDeserialization() {
        val jsonString = """
            {
                "chiSquareValue": 5.99,
                "pValue": 0.05,
                "significanceLevel": 0.05,
                "followsBenford": true,
                "observedCounts": [100, 50, 30],
                "expectedCounts": [95.0, 55.0, 30.0]
            }
        """.trimIndent()

        val response = json.decodeFromString<AppResponse>(jsonString)
        assertEquals(5.99, response.chiSquareValue, 1e-6)
        assertEquals(0.05, response.pValue, 1e-6)
        assertEquals(0.05, response.significanceLevel, 1e-6)
        assertTrue(response.followsBenford)
        assertEquals(listOf(100L, 50L, 30L), response.observedCounts)
        assertEquals(listOf(95.0, 55.0, 30.0), response.expectedCounts)
    }

    @Test
    fun testEmptyCounts() {
        val response = AppResponse(
            chiSquareValue = 0.0,
            pValue = 1.0,
            significanceLevel = 0.05,
            followsBenford = true,
            observedCounts = emptyList(),
            expectedCounts = emptyList()
        )

        assertEquals(0.0, response.chiSquareValue)
        assertEquals(1.0, response.pValue)
        assertTrue(response.followsBenford)
        assertTrue(response.observedCounts.isEmpty())
        assertTrue(response.expectedCounts.isEmpty())
    }

}
