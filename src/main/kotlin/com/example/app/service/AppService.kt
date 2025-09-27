package com.example.app.service

import com.example.app.models.AppResponse
import com.example.app.utils.AppUtils
import com.example.app.utils.BenfordLawUtils

class AppService {
    fun calculateBenfordDistribution(text: String, significance: Double): AppResponse {
        val numbers = AppUtils.extractNumbersFromText(text)
        val observedCounts = AppUtils.countLeadingDigits(numbers)
        val expectedCounts = BenfordLawUtils.expectedCount(numbers.size)
        return BenfordLawUtils.performBenfordsLawTest(observedCounts, expectedCounts,significance)
    }
}