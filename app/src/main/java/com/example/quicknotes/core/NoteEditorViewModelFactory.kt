package com.example.quicknotes.core

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quicknotes.presentation.notes.NoteEditorViewModel
import java.util.UUID

class NoteEditorViewModelFactory(
    private val dependencies: AppDependencies,
    private val savedStateHandle: SavedStateHandle,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val noteIdStr = savedStateHandle.get<String>("noteId")
        val noteId = noteIdStr?.let { UUID.fromString(it) }
        return dependencies.createNoteEditorViewModel(noteId) as T
    }
}
