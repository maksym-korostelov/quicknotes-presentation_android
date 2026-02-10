package com.example.quicknotes.presentation.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.usecase.DeleteNoteUseCase
import com.example.quicknotes.domain.usecase.GetNoteUseCase
import com.example.quicknotes.domain.usecase.SaveNoteUseCase
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteDetailUiState(
    val note: Note? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class NoteDetailViewModel(
    private val noteId: UUID,
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailUiState(isLoading = true))
    val state: StateFlow<NoteDetailUiState> = _state.asStateFlow()

    init {
        loadNote()
    }

    fun loadNote() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val note = getNoteUseCase.execute(noteId)
                _state.update { it.copy(note = note, isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load note",
                    )
                }
            }
        }
    }

    fun togglePin() {
        val note = _state.value.note ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val updated = note.copy(isPinned = !note.isPinned, modifiedAt = Instant.now())
                saveNoteUseCase.execute(updated)
                _state.update { it.copy(note = updated, isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to update",
                    )
                }
            }
        }
    }

    fun toggleArchive() {
        val note = _state.value.note ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val updated = note.copy(isArchived = !note.isArchived, modifiedAt = Instant.now())
                saveNoteUseCase.execute(updated)
                _state.update { it.copy(note = updated, isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to update",
                    )
                }
            }
        }
    }

    fun toggleCompleted() {
        val note = _state.value.note ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val updated = note.copy(isCompleted = !note.isCompleted, modifiedAt = Instant.now())
                saveNoteUseCase.execute(updated)
                _state.update { it.copy(note = updated, isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to update",
                    )
                }
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                deleteNoteUseCase.execute(noteId)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to delete",
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
