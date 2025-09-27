package com.example.app.models

import kotlinx.serialization.Serializable

@Serializable
data class AppResponse(
    val chiSquareValue: Double,
    val pValue: Double,
    val significanceLevel: Double,
    val followsBenford: Boolean,
    val observedCounts: List<Long>,
    val expectedCounts: List<Double>
)