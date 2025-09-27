package com.example.app.models

import kotlinx.serialization.Serializable


@Serializable
data class AppRequest (
    val text: String,
    val significanceLevel: Double = 0.05
)