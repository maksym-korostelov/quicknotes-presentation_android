package com.example.quicknotes.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quicknotes.presentation.notes.NoteDetailViewModel
import java.util.UUID

class NoteDetailViewModelFactory(
    private val dependencies: AppDependencies,
    private val noteId: UUID,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return dependencies.createNoteDetailViewModel(noteId) as T
    }
}
