package com.example.app.utils
import com.example.app.exceptions.ParsingException


object AppUtils {
    private val regex = Regex(
        """-?(?:\d{1,3}(?:[.,]\d{3})+|\d+)(?:[.,]\d+)?"""
    )


    fun extractNumbersFromText(text: String): List<Double> {
        val matches = regex.findAll(text).map { it.value }.toList()

        if (matches.isEmpty()) {
            throw ParsingException("No numbers found in input text")
        }

        return matches.map { raw ->
            val normalized = normalizeNumber(raw)
            normalized.toDoubleOrNull()
                ?: throw ParsingException("Cannot parse number: '$raw' after normalization: '$normalized'")
        }
    }

    fun normalizeNumber(raw: String): String {
        return if (raw.contains(',') && raw.lastIndexOf(',') > raw.lastIndexOf('.')) {
            raw.replace(".", "").replace(",", ".") // EU format
        } else {
            raw.replace(",", "") // US format
        }
    }

    fun getLeadingDigit(value: Double): Int? {
        var v = kotlin.math.abs(value)
        if (v == 0.0) return null
        // Shift decimal until >=1
        while (v < 1.0) v *= 10.0
        while (v >= 10.0) v /= 10.0
        val d = v.toInt()
        return if (d in 1..9) d else null
    }

    fun countLeadingDigits(numbers: List<Double>): LongArray {
        val observedCounts = LongArray(9) { 0 }
        for (n in numbers) {
            val d = getLeadingDigit(n)
            if (d != null) {
                observedCounts[d-1] = observedCounts[d-1] + 1
            }
        }
        return observedCounts
    }



}