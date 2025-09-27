package com.example.app.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class BenfordLawUtilsTest {

    @Test
    fun testPerformBenfordsLawSuccess() {
        val total = 1000
        val expected = BenfordLawUtils.expectedCount(total)

        // Construct observed counts close to expected
        val observed = expected.map { it.toLong() }.toLongArray()

        val response = BenfordLawUtils.performBenfordsLawTest(observed, expected, 0.05)

        assertTrue(response.followsBenford, "Not following Benford’s law")
        assertTrue(response.pValue >= 0.05)
    }

    @Test
    fun testPerformBenfordsLawFailure() {
        val total = 1000
        val expected = BenfordLawUtils.expectedCount(total)

        // All counts forced to start with digit 9 (bad distribution)
        val observed = LongArray(9) { if (it == 8) total.toLong() else 0L }

        val response = BenfordLawUtils.performBenfordsLawTest(observed, expected, 0.05)

        assertFalse(response.followsBenford, "follows Benford’s law")
        assertTrue(response.pValue < 0.05)
    }


    @Test
    fun testPerformBenfordsLawWithSignificance() {
        val total = 1000
        val expected = BenfordLawUtils.expectedCount(total)
        val observed = expected.map { it.toLong() }.toLongArray()

        // Use higher significance level
        val response = BenfordLawUtils.performBenfordsLawTest(observed, expected, 0.10)

        assertTrue(response.followsBenford, "Should follow with relaxed significance")
    }
}
