package com.example.quicknotes.data.local

import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.data.local.entity.CategoryEntity
import com.example.quicknotes.data.local.entity.NoteEntity
import java.time.Instant
import java.util.UUID

fun CategoryEntity.toDomain(): Category = Category(
    id = UUID.fromString(id),
    name = name,
    icon = icon,
    colorHex = colorHex,
    createdAt = Instant.ofEpochMilli(createdAtMillis),
    modifiedAt = Instant.ofEpochMilli(modifiedAtMillis),
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id.toString(),
    name = name,
    icon = icon,
    colorHex = colorHex,
    createdAtMillis = createdAt.toEpochMilli(),
    modifiedAtMillis = modifiedAt.toEpochMilli(),
)

fun NoteEntity.toDomain(category: Category?): Note = Note(
    id = UUID.fromString(id),
    title = title,
    content = content,
    category = category,
    isPinned = isPinned,
    isArchived = isArchived,
    isCompleted = isCompleted,
    createdAt = Instant.ofEpochMilli(createdAtMillis),
    modifiedAt = Instant.ofEpochMilli(modifiedAtMillis),
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id.toString(),
    title = title,
    content = content,
    categoryId = category?.id?.toString(),
    isPinned = isPinned,
    isArchived = isArchived,
    isCompleted = isCompleted,
    createdAtMillis = createdAt.toEpochMilli(),
    modifiedAtMillis = modifiedAt.toEpochMilli(),
)
