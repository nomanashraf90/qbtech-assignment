package com.example.app.models

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class AppResponseTest {

    private val json = Json { prettyPrint = true }

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
