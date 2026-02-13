package com.example.quicknotes.domain.usecase

import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.repository.NoteRepository
class SaveNoteUseCase constructor(
    private val repository: NoteRepository,
) {
    suspend fun execute(note: Note) {
        if (note.title.isBlank()) throw SaveNoteError.EmptyTitle
        repository.saveNote(note)
    }
}

sealed class SaveNoteError : Exception() {
    object EmptyTitle : SaveNoteError()
}
