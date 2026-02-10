package com.example.quicknotes.domain.usecase

import com.example.quicknotes.domain.repository.NoteRepository
import java.util.UUID
class DeleteNoteUseCase constructor(
    private val repository: NoteRepository,
) {
    suspend fun execute(id: UUID) = repository.deleteNote(id)
}
