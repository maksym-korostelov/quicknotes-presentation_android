package com.example.quicknotes.domain.repository

import com.example.quicknotes.domain.entity.Note
import java.util.UUID

/**
 * Protocol defining operations for note data persistence.
 */
interface NoteRepository {

    /** Fetches all notes. */
    suspend fun fetchNotes(): List<Note>

    /** Fetches a specific note by ID. */
    suspend fun fetchNote(id: UUID): Note?

    /** Saves a new note or updates an existing one. */
    suspend fun saveNote(note: Note)

    /** Updates an existing note. */
    suspend fun updateNote(note: Note)

    /** Deletes a note by ID. */
    suspend fun deleteNote(id: UUID)
}
