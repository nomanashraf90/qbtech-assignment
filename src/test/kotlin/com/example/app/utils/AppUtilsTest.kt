package com.example.app.utils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

class AppUtilsTest {

    @Nested
    @DisplayName("normalizeNumber Tests")
    inner class NormalizeNumberTest {

        @Test
        @DisplayName("Should normalize EU format numbers correctly")
        fun `should normalize EU format numbers correctly`() {
            assertEquals("1234.56", AppUtils.normalizeNumber("1.234,56"))
            assertEquals("1234567.89", AppUtils.normalizeNumber("1.234.567,89"))
            assertEquals("123.45", AppUtils.normalizeNumber("123,45"))
            assertEquals("1.1", AppUtils.normalizeNumber("1,1"))
            assertEquals("0.0", AppUtils.normalizeNumber("0,0"))
            assertEquals("-1234.56", AppUtils.normalizeNumber("-1.234,56"))
        }

        @Test
        @DisplayName("Should normalize US format numbers correctly")
        fun `should normalize US format numbers correctly`() {
            assertEquals("1234.56", AppUtils.normalizeNumber("1,234.56"))
            assertEquals("1234567.89", AppUtils.normalizeNumber("1,234,567.89"))
            assertEquals("123.45", AppUtils.normalizeNumber("123.45"))
            assertEquals("1.1", AppUtils.normalizeNumber("1.1"))
            assertEquals("0.0", AppUtils.normalizeNumber("0.0"))
            assertEquals("-1234.56", AppUtils.normalizeNumber("-1,234.56"))
        }

        @Test
        @DisplayName("Should handle numbers without thousands separators")
        fun `should handle numbers without thousands separators`() {
            assertEquals("1234.56", AppUtils.normalizeNumber("1234,56"))
            assertEquals("1234.56", AppUtils.normalizeNumber("1234.56"))
            assertEquals("-567.89", AppUtils.normalizeNumber("-567,89"))
        }

        @Test
        @DisplayName("Should handle ambiguous format with comma as last separator")
        fun `should handle ambiguous format with comma as last separator`() {
            assertEquals("1.2", AppUtils.normalizeNumber("1,2"))
            assertEquals("12.3", AppUtils.normalizeNumber("1,2.3"))
        }

        @Test
        @DisplayName("Should handle numbers with multiple decimal points")
        fun `should handle numbers with multiple decimal points`() {
            assertEquals("1234567.89", AppUtils.normalizeNumber("1.234.567,89"))
            assertEquals("1234567.89", AppUtils.normalizeNumber("1,234,567.89"))
        }
    }

    @Nested
    @DisplayName("extractNumbersFromText Tests")
    inner class ExtractNumbersFromTextTest {

        @Test
        @DisplayName("Should extract multiple numbers from mixed text")
        fun `should extract multiple numbers from mixed text`() {
            val text = "Sales: $1,234.56 and €1.234,57 with weight 12.5kg"
            val result = AppUtils.extractNumbersFromText(text)

            assertEquals(3, result.size)
            assertEquals(1234.56, result[0], 0.001)
            assertEquals(1234.57, result[1], 0.001)
            assertEquals(12.5, result[2], 0.001)
        }

        @Test
        @DisplayName("Should extract negative numbers from text")
        fun `should extract negative numbers from text`() {
            val text = "Temperatures: -12.5°C, -1.234,56°C, +45.6°F"
            val result = AppUtils.extractNumbersFromText(text)

            assertEquals(3, result.size)
            assertEquals(-12.5, result[0], 0.001)
            assertEquals(-1234.56, result[1], 0.001)
            assertEquals(45.6, result[2], 0.001)
        }

        @Test
        @DisplayName("Should return empty list for text with no numbers")
        fun `should return empty list for text with no numbers`() {
            val text = "Hello world without any numbers here"
            val result = AppUtils.extractNumbersFromText(text)
            assertTrue(result.isEmpty())
        }

        @Test
        @DisplayName("Should handle empty string input")
        fun `should handle empty string input`() {
            val result = AppUtils.extractNumbersFromText("")
            assertTrue(result.isEmpty())
        }

        @Test
        @DisplayName("Should handle text with only whitespace")
        fun `should handle text with only whitespace`() {
            val result = AppUtils.extractNumbersFromText("   \n\t  ")
            assertTrue(result.isEmpty())
        }

        @Test
        @DisplayName("Should extract numbers with mixed formats in same text")
        fun `should extract mixed format numbers in same text`() {
            val text = "US: 1,234.56 EU: 1.234,57 Plain: 1234.58 Negative: -2.345,67"
            val result = AppUtils.extractNumbersFromText(text)

            assertEquals(4, result.size)
            assertEquals(1234.56, result[0], 0.001)
            assertEquals(1234.57, result[1], 0.001)
            assertEquals(1234.58, result[2], 0.001)
            assertEquals(-2345.67, result[3], 0.001)
        }

        @Test
        @DisplayName("Should ignore invalid number patterns")
        fun `should ignore invalid number patterns`() {
            val text = "Valid: 1,234.56 Invalid: 1.234.567.89 Text: hello 123,456,789.12"
            val result = AppUtils.extractNumbersFromText(text)

            assertEquals(2, result.size)
            assertEquals(1234.56, result[0], 0.001)
            assertEquals(123456789.12, result[1], 0.001)
        }

        @Test
        @DisplayName("Should handle numbers at boundaries of text")
        fun `should handle numbers at boundaries of text`() {
            val text = "123.45 starts with number and ends with 678.90"
            val result = AppUtils.extractNumbersFromText(text)

            assertEquals(2, result.size)
            assertEquals(123.45, result[0], 0.001)
            assertEquals(678.90, result[1], 0.001)
        }

        @Test
        @DisplayName("Should handle text with only numbers")
        fun `should handle text with only numbers`() {
            val text = "123.45 678.90"
            val result = AppUtils.extractNumbersFromText(text)

            assertEquals(2, result.size)
            assertEquals(123.45, result[0], 0.001)
            assertEquals(678.90, result[1], 0.001)
        }
    }

    @Nested
    @DisplayName("getLeadingDigit Tests")
    inner class GetLeadingDigitTest {

        @Test
        @DisplayName("Should return correct leading digit for positive numbers >= 1")
        fun `should return correct leading digit for positive numbers greater than or equal to 1`() {
            assertEquals(1, AppUtils.getLeadingDigit(123.45))
            assertEquals(4, AppUtils.getLeadingDigit(45.67))
            assertEquals(7, AppUtils.getLeadingDigit(7.89))
            assertEquals(9, AppUtils.getLeadingDigit(9.0))
            assertEquals(1, AppUtils.getLeadingDigit(1000.0))
            assertEquals(9, AppUtils.getLeadingDigit(9999.99))
        }

        @Test
        @DisplayName("Should return correct leading digit for numbers between 0 and 1")
        fun `should return correct leading digit for numbers between 0 and 1`() {
            assertEquals(1, AppUtils.getLeadingDigit(0.123))
            assertEquals(4, AppUtils.getLeadingDigit(0.0456))
            assertEquals(7, AppUtils.getLeadingDigit(0.00789))
            assertEquals(9, AppUtils.getLeadingDigit(0.999))
            assertEquals(1, AppUtils.getLeadingDigit(0.000001234))
        }

        @Test
        @DisplayName("Should return correct leading digit for negative numbers")
        fun `should return correct leading digit for negative numbers`() {
            assertEquals(1, AppUtils.getLeadingDigit(-123.45))
            assertEquals(4, AppUtils.getLeadingDigit(-45.67))
            assertEquals(1, AppUtils.getLeadingDigit(-0.123))
            assertEquals(4, AppUtils.getLeadingDigit(-0.0456))
            assertEquals(9, AppUtils.getLeadingDigit(-999.99))
        }

        @Test
        @DisplayName("Should return null for zero")
        fun `should return null for zero`() {
            assertNull(AppUtils.getLeadingDigit(0.0))
            assertNull(AppUtils.getLeadingDigit(-0.0))
        }

        @Test
        @DisplayName("Should handle very small positive numbers")
        fun `should handle very small positive numbers`() {
            assertEquals(1, AppUtils.getLeadingDigit(1.646411501299294E-5))
            assertEquals(5, AppUtils.getLeadingDigit(5.0E-10))
            assertEquals(9, AppUtils.getLeadingDigit(9.999E-15))
        }

        @Test
        @DisplayName("Should handle very small negative numbers")
        fun `should handle very small negative numbers`() {
            assertEquals(1, AppUtils.getLeadingDigit(-1.646411501299294E-5))
            assertEquals(5, AppUtils.getLeadingDigit(-5.0E-10))
        }

        @Test
        @DisplayName("Should handle very large numbers")
        fun `should handle very large numbers`() {
            assertEquals(1, AppUtils.getLeadingDigit(1.23456E+20))
            assertEquals(7, AppUtils.getLeadingDigit(7.89E+15))
            assertEquals(9, AppUtils.getLeadingDigit(9.876E+30))
        }

        @Test
        @DisplayName("Should handle numbers that normalize exactly to 1.0")
        fun `should handle numbers that normalize exactly to 1`() {
            assertEquals(1, AppUtils.getLeadingDigit(100.0))
            assertEquals(1, AppUtils.getLeadingDigit(1000.0))
            assertEquals(1, AppUtils.getLeadingDigit(0.1))
            assertEquals(1, AppUtils.getLeadingDigit(0.01))
        }

        @Test
        @DisplayName("Should return null for numbers that lead to invalid digits")
        fun `should return null for numbers that lead to invalid digits`() {
            // This test ensures the function handles edge cases properly
            // The current implementation should always return digits 1-9 or null
            assertNull(AppUtils.getLeadingDigit(0.0))
        }
    }

    @Nested
    @DisplayName("countLeadingDigits Tests")
    inner class CountLeadingDigitsTest {

        @Test
        @DisplayName("Should correctly count leading digits in mixed list")
        fun `should correctly count leading digits in mixed list`() {
            val numbers = listOf(123.0, 45.6, 7.89, 123.45, 456.78, 0.789, 0.0456, 890.12)
            val result = AppUtils.countLeadingDigits(numbers)

            val expected = longArrayOf(2, 0, 0, 3, 0, 0, 2, 1, 0) // 1:2, 4:1, 7:1, 8:2, 4:1
            assertArrayEquals(expected, result)
        }

        @Test
        @DisplayName("Should return zero array for empty list")
        fun `should return zero array for empty list`() {
            val result = AppUtils.countLeadingDigits(emptyList())
            val expected = longArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
            assertArrayEquals(expected, result)
        }

        @Test
        @DisplayName("Should ignore zeros in the input list")
        fun `should ignore zeros in the input list`() {
            val numbers = listOf(0.0, 123.0, -0.0, 456.0, 0.0, 789.0)
            val result = AppUtils.countLeadingDigits(numbers)

            val expected = longArrayOf(1, 0, 0, 1, 0, 0, 1, 0, 0) // 1:1, 4:1, 7:1
            assertArrayEquals(expected, result)
        }

        @Test
        @DisplayName("Should handle list with all digits 1-9")
        fun `should handle list with all digits 1-9`() {
            val numbers = (1..9).map { it * 100.0 } + (1..9).map { it * 0.01 }
            val result = AppUtils.countLeadingDigits(numbers)

            val expected = longArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2) // Each digit appears twice
            assertArrayEquals(expected, result)
        }

        @Test
        @DisplayName("Should handle list with only one leading digit")
        fun `should handle list with only one leading digit`() {
            val numbers = listOf(1.23, 12.34, 123.45, 0.123, 0.0123)
            val result = AppUtils.countLeadingDigits(numbers)

            val expected = longArrayOf(5, 0, 0, 0, 0, 0, 0, 0, 0) // All numbers start with 1
            assertArrayEquals(expected, result)
        }

        @Test
        @DisplayName("Should handle mixed positive and negative numbers")
        fun `should handle mixed positive and negative numbers`() {
            val numbers = listOf(123.0, -45.6, 789.0, -0.123, 456.0, -0.0789)
            val result = AppUtils.countLeadingDigits(numbers)

            val expected = longArrayOf(2, 0, 0, 2, 0, 0, 2, 0, 0)
            assertArrayEquals(expected, result)
        }

        @Test
        @DisplayName("Should handle single number list")
        fun `should handle single number list`() {
            val numbers = listOf(123.45)
            val result = AppUtils.countLeadingDigits(numbers)

            val expected = longArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0) // Only digit 1
            assertArrayEquals(expected, result)
        }

        @Test
        @DisplayName("Should handle list with numbers having same leading digit")
        fun `should handle list with numbers having same leading digit`() {
            val numbers = listOf(1.23, 10.0, 100.0, 0.123, 0.0123)
            val result = AppUtils.countLeadingDigits(numbers)

            val expected = longArrayOf(5, 0, 0, 0, 0, 0, 0, 0, 0) // All numbers start with 1
            assertArrayEquals(expected, result)
        }

        @Test
        @DisplayName("Should handle very large list efficiently")
        fun `should handle very large list efficiently`() {
            val numbers = List(100) { (it + 1).toDouble() } // Numbers 1.0 to 100.0
            val result = AppUtils.countLeadingDigits(numbers)

            // All numbers should be counted (no zeros in this range)
            assertEquals(100, result.sum())
            // Should follow Benford's law distribution roughly
            assertTrue(result[0] > result[1]) // More 1s than 2s
            assertTrue(result[0] > result[8]) // More 1s than 9s
        }
    }

    @Test
    @DisplayName("Integration test - complete pipeline from text to digit counts")
    fun `integration test complete pipeline from text to digit counts`() {
        val text = """
            Quarterly sales: $1,234.56, €2.345,67, $3,456.78, €4.567,89
            Additional revenue: $12,345.67 and €98.765,43
            Negative values: -$567.89, -€1.234,56
        """.trimIndent()

        val numbers = AppUtils.extractNumbersFromText(text)
        val leadingDigits = AppUtils.countLeadingDigits(numbers)

        assertEquals(8, numbers.size)
        // Expected numbers: 1234.56, 2345.67, 3456.78, 4567.89, 12345.67, 98765.43, -567.89, -1234.56
        // Leading digits: 1, 2, 3, 4, 1, 9, 5, 1 → Counts: 1:3, 2:1, 3:1, 4:1, 5:1, 9:1
        val expected = longArrayOf(3, 1, 1, 1, 1, 0, 0, 0, 1)
        assertArrayEquals(expected, leadingDigits)
    }

    @Test
    @DisplayName("Edge case test - numbers with scientific notation in text")
    fun `edge case test numbers with scientific notation in text`() {
        val text = "Scientific numbers: 1.23e-4, 5.67E+8 but regex should ignore scientific notation"
        val result = AppUtils.extractNumbersFromText(text)

        // The current regex doesn't capture scientific notation, so it should only capture 1.23 and 5.67
        assertTrue(result.size >= 2)
    }

    @Test
    @DisplayName("Edge case test - numbers with currency symbols and special characters")
    fun `edge case test numbers with currency symbols and special characters`() {
        val text = "Price: $1,234.56, Discount: 25%, Tax: 7.5%, Total: €1.234,57"
        val result = AppUtils.extractNumbersFromText(text)

        // Should extract numbers ignoring currency symbols and percentages
        assertTrue(result.contains(1234.56))
        assertTrue(result.contains(1234.57))
        // 25% and 7.5% should also be extracted as numbers
        assertTrue(result.contains(25.0) || result.contains(7.5))
    }
}