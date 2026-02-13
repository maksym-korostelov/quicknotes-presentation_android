package com.example.quicknotes.data.repository

import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.repository.NoteRepository
import java.util.UUID
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryNoteRepository(
    seedNotes: List<Note> = emptyList(),
) : NoteRepository {

    private val mutex = Mutex()
    private val notes = seedNotes.toMutableList()

    override suspend fun fetchNotes(): List<Note> = mutex.withLock {
        notes.sortedByDescending { it.modifiedAt }
    }

    override suspend fun fetchNote(id: UUID): Note? = mutex.withLock {
        notes.find { it.id == id }
    }

    override suspend fun saveNote(note: Note) {
        mutex.withLock {
            val index = notes.indexOfFirst { it.id == note.id }
            if (index >= 0) {
                notes[index] = note
            } else {
                notes.add(0, note)
            }
        }
    }

    override suspend fun updateNote(note: Note) {
        mutex.withLock {
            val index = notes.indexOfFirst { it.id == note.id }
            if (index >= 0) notes[index] = note
        }
    }

    override suspend fun deleteNote(id: UUID) {
        mutex.withLock { notes.removeAll { it.id == id } }
    }
}
