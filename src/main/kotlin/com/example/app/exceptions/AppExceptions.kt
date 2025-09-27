package com.example.app.exceptions

/**
 * Base class for all application-specific exceptions.
 * Allows centralized handling in Ktor.
 */
sealed class AppException(message: String) : RuntimeException(message)

/**
 * Thrown when request input values are invalid
 * (e.g. significanceLevel <= 0 or >= 1).
 */
class InvalidInputException(message: String) : AppException(message)

/**
 * Thrown when parsing text → numbers fails.
 */
class ParsingException(message: String) : AppException(message)