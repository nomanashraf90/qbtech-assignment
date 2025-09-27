package com.example.app.service

import com.example.app.models.AppResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class AppServiceTest {

    private val service = AppService()

    @Test
    fun `test numbers follow Benford distribution`() {
        val text = """
            Invoice amounts: 123, 1456, 1789, 2345, 28999, 345, 3890,
            4567, 489, 5678, 6789, 7890, 89012, 912345
        """.trimIndent()

        val significance = 0.05
        val response: AppResponse = service.calculateBenfordDistribution(text, significance)

        println("Response = $response")

        // Assertions
        assertEquals(9, response.expectedCounts.size, "Expected 9 categories (digits 1–9)")
        assertEquals(9, response.observedCounts.size, "Observed must also have 9 categories")
        assertEquals(true, response.followsBenford, "Following benford law")
    }

    @Test
    fun `test random numbers may not follow Benford distribution`() {
        val text = (1..1000).joinToString(" ") { it.toString() } // sequential numbers 1–1000

        val significance = 0.05
        val response: AppResponse = service.calculateBenfordDistribution(text, significance)

        println("Response = $response")

        assertEquals(9, response.expectedCounts.size, "Expected 9 categories (digits 1–9)")
        assertEquals(9, response.observedCounts.size, "Observed must also have 9 categories")
        assertEquals(false, response.followsBenford, "Not following benford law")
    }
}
