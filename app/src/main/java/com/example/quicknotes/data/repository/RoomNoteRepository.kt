package com.example.quicknotes.data.repository

import com.example.quicknotes.data.local.dao.CategoryDao
import com.example.quicknotes.data.local.dao.NoteDao
import com.example.quicknotes.data.local.toDomain
import com.example.quicknotes.data.local.toEntity
import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.repository.NoteRepository
import java.util.UUID

class RoomNoteRepository(
    private val noteDao: NoteDao,
    private val categoryDao: CategoryDao,
) : NoteRepository {

    override suspend fun fetchNotes(): List<Note> {
        val notes = noteDao.getAll()
        val categoriesById = categoryDao.getAll().associateBy { it.id }
        return notes.map { entity ->
            val category = entity.categoryId?.let { categoriesById[it]?.toDomain() }
            entity.toDomain(category)
        }
    }

    override suspend fun fetchNote(id: UUID): Note? {
        val entity = noteDao.getById(id.toString()) ?: return null
        val category = entity.categoryId?.let { categoryDao.getById(it)?.toDomain() }
        return entity.toDomain(category)
    }

    override suspend fun saveNote(note: Note) {
        noteDao.insert(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        noteDao.update(note.toEntity())
    }

    override suspend fun deleteNote(id: UUID) {
        noteDao.deleteById(id.toString())
    }
}
