package com.example.quicknotes.domain.entity

import java.time.Instant
import java.util.UUID

/**
 * Represents a user's note in the QuickNotes app.
 */
data class Note(
    val id: UUID,
    val title: String,
    val content: String,
    val category: Category?,
    val isPinned: Boolean,
    val isArchived: Boolean,
    val isCompleted: Boolean,
    val createdAt: Instant,
    val modifiedAt: Instant,
) {
    constructor(
        title: String,
        content: String,
        category: Category? = null,
        isPinned: Boolean = false,
        isArchived: Boolean = false,
        isCompleted: Boolean = false,
    ) : this(
        id = UUID.randomUUID(),
        title = title,
        content = content,
        category = category,
        isPinned = isPinned,
        isArchived = isArchived,
        isCompleted = isCompleted,
        createdAt = Instant.now(),
        modifiedAt = Instant.now(),
    )
}
