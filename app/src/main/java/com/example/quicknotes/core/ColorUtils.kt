package com.example.quicknotes.core

import androidx.compose.ui.graphics.Color

/**
 * Parses a hex color string (e.g. "3B82F6" or "#3B82F6") to Compose Color.
 */
fun parseColorHex(hex: String): Color {
    val clean = hex.removePrefix("#").trim()
    val long = when (clean.length) {
        6 -> clean.toLongOrNull(16)?.let { 0xFF000000L or it }
        8 -> clean.toLongOrNull(16)
        else -> null
    }
    return long?.let { Color(it) } ?: Color(0xFF000000)
}
