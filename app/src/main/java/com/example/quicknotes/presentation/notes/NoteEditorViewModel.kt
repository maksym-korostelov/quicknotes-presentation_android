package com.example.quicknotes.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.usecase.GetCategoriesUseCase
import com.example.quicknotes.domain.usecase.GetNoteUseCase
import com.example.quicknotes.domain.usecase.SaveNoteUseCase
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoteEditorUiState(
    val title: String = "",
    val content: String = "",
    val selectedCategory: Category? = null,
    val isPinned: Boolean = false,
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
) {
    val isValid: Boolean get() = title.isNotBlank()
}

class NoteEditorViewModel(
    private val existingNoteId: UUID?,
    private val getNoteUseCase: GetNoteUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(NoteEditorUiState())
    val state: StateFlow<NoteEditorUiState> = _state.asStateFlow()

    private var loadedNote: Note? = null

    init {
        loadCategories()
        if (existingNoteId != null) {
            loadNote()
        }
    }

    private fun loadNote() {
        val id = existingNoteId ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val note = getNoteUseCase.execute(id)
                loadedNote = note
                if (note != null) {
                    _state.update {
                        it.copy(
                            title = note.title,
                            content = note.content,
                            selectedCategory = note.category,
                            isPinned = note.isPinned,
                            isLoading = false,
                        )
                    }
                } else {
                    _state.update { it.copy(isLoading = false) }
                }
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

    fun loadCategories() {
        viewModelScope.launch {
            try {
                val categories = getCategoriesUseCase.execute()
                _state.update { it.copy(categories = categories) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(errorMessage = e.message ?: "Failed to load categories")
                }
            }
        }
    }

    fun setTitle(title: String) {
        _state.update { it.copy(title = title) }
    }

    fun setContent(content: String) {
        _state.update { it.copy(content = content) }
    }

    fun setSelectedCategory(category: Category?) {
        _state.update { it.copy(selectedCategory = category) }
    }

    fun setPinned(pinned: Boolean) {
        _state.update { it.copy(isPinned = pinned) }
    }

    fun save() {
        viewModelScope.launch {
            val s = _state.value
            if (!s.isValid) return@launch
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val note = Note(
                    id = loadedNote?.id ?: UUID.randomUUID(),
                    title = s.title.trim(),
                    content = s.content,
                    category = s.selectedCategory,
                    isPinned = s.isPinned,
                    isArchived = loadedNote?.isArchived ?: false,
                    isCompleted = loadedNote?.isCompleted ?: false,
                    createdAt = loadedNote?.createdAt ?: Instant.now(),
                    modifiedAt = Instant.now(),
                )
                saveNoteUseCase.execute(note)
                _state.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to save",
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
