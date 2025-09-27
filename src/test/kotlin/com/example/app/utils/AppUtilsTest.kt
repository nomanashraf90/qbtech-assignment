package com.example.app.utils

import com.example.app.exceptions.ParsingException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*

class AppUtilsTest {

    @Test
    fun `extractNumbersFromText should parse US numbers correctly`() {
        val text = "Invoice 12345, 6789, -45.6"
        val numbers = AppUtils.extractNumbersFromText(text)
        assertEquals(listOf(12345.0, 6789.0, -45.6), numbers)
    }

    @Test
    fun `extractNumbersFromText should parse EU numbers correctly`() {
        val text = "EU amounts: 1.234,56 and 12.345,67"
        val numbers = AppUtils.extractNumbersFromText(text)
        assertEquals(listOf(1234.56, 12345.67), numbers)
    }

    @Test
    fun `extractNumbersFromText should throw ParsingException for no numbers`() {
        val text = "No numbers here!"
        assertThrows<ParsingException> {
            AppUtils.extractNumbersFromText(text)
        }
    }

    @Test
    fun `normalizeNumber converts EU format correctly`() {
        val eu = "1.234,56"
        val normalized = AppUtils.normalizeNumber(eu)
        assertEquals("1234.56", normalized)
    }

    @Test
    fun `normalizeNumber converts US format correctly`() {
        val us = "12,345.67"
        val normalized = AppUtils.normalizeNumber(us)
        assertEquals("12345.67", normalized)
    }

    @Test
    fun `getLeadingDigit works for positive integers`() {
        assertEquals(1, AppUtils.getLeadingDigit(123.0))
        assertEquals(9, AppUtils.getLeadingDigit(9876.0))
    }

    @Test
    fun `getLeadingDigit works for decimals and negative numbers`() {
        assertEquals(1, AppUtils.getLeadingDigit(0.123))
        assertEquals(7, AppUtils.getLeadingDigit(-0.789))
    }

    @Test
    fun `getLeadingDigit returns null for zero`() {
        assertNull(AppUtils.getLeadingDigit(0.0))
    }

    @Test
    fun `countLeadingDigits returns correct counts`() {
        val numbers = listOf(123.0, 145.0, 178.0, 289.0, 345.0, 0.456, -567.0)
        val counts = AppUtils.countLeadingDigits(numbers)
        // Digits: 1->2, 2->1, 3->1, 4->1, 5->1
        val expected = longArrayOf(3,1,1,1,1,0,0,0,0)
        assertArrayEquals(expected, counts)
    }
}
