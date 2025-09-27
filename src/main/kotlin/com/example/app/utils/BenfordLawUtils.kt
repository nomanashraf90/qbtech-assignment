package com.example.app.utils

import com.example.app.models.AppResponse
import org.apache.commons.math3.stat.inference.ChiSquareTest
import kotlin.math.log10
object BenfordLawUtils {

    fun expectedCount(total: Int): DoubleArray {
        val expected = DoubleArray(9) { i ->
            total * log10(1 + 1.0 / (i + 1))
        }
        return expected
    }


    fun performBenfordsLawTest(observedCounts: LongArray, expectedCounts: DoubleArray, significance: Double): AppResponse {
            val chiTest = ChiSquareTest()
        val chiSquare = chiTest.chiSquare(expectedCounts, observedCounts)
        val pValue = chiTest.chiSquareTest(expectedCounts, observedCounts)

        val followsBenford = pValue >= significance

        return AppResponse(
            chiSquareValue = chiSquare,
            pValue = pValue,
            significanceLevel= significance,
            followsBenford= followsBenford,
            observedCounts= observedCounts.toList(),
            expectedCounts= expectedCounts.toList()
        )
    }

}