package com.example.quicknotes.domain.entity

import java.time.Instant
import java.util.UUID

/**
 * Represents a category that organizes notes in the QuickNotes app.
 */
data class Category(
    val id: UUID,
    val name: String,
    val icon: String,
    val colorHex: String,
    val createdAt: Instant,
    val modifiedAt: Instant,
) {
    constructor(
        name: String,
        icon: String = "folder",
        colorHex: String = "3B82F6",
    ) : this(
        id = UUID.randomUUID(),
        name = name,
        icon = icon,
        colorHex = colorHex,
        createdAt = Instant.now(),
        modifiedAt = Instant.now(),
    )
}
