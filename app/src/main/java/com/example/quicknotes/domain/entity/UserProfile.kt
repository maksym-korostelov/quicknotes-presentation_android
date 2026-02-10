package com.example.quicknotes.domain.entity

import java.time.Instant
import java.util.UUID

/**
 * Represents the current user's profile (stats and display info).
 */
data class UserProfile(
    val id: UUID = UUID.randomUUID(),
    val displayName: String,
    val email: String,
    val avatarUrl: String? = null,
    val joinedDate: Instant = Instant.now(),
    val notesCount: Int = 0,
    val categoriesCount: Int = 0,
)
