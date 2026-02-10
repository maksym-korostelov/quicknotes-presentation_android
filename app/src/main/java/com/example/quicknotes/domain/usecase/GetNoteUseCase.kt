package com.example.quicknotes.domain.usecase

import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.repository.NoteRepository
import java.util.UUID
class GetNoteUseCase constructor(
    private val repository: NoteRepository,
) {
    suspend fun execute(id: UUID): Note? = repository.fetchNote(id)
}
