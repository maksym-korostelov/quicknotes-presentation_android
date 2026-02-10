package com.example.quicknotes.domain.usecase

import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.repository.NoteRepository
class GetNotesUseCase constructor(
    private val repository: NoteRepository,
) {
    suspend fun execute(): List<Note> = repository.fetchNotes()
}
