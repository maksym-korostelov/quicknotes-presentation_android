package com.example.quicknotes.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.usecase.DeleteNoteUseCase
import com.example.quicknotes.domain.usecase.GetCategoriesUseCase
import com.example.quicknotes.domain.usecase.GetNotesUseCase
import com.example.quicknotes.domain.usecase.SaveNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class NoteListUiState(
    val notes: List<Note> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: UUID? = null,
    val showArchivedAndCompleted: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val filteredNotes: List<Note>
        get() {
            var result = notes
            if (!showArchivedAndCompleted) {
                result = result.filter { !it.isArchived && !it.isCompleted }
            }
            selectedCategoryId?.let { id ->
                result = result.filter { it.category?.id == id }
            }
            val query = searchQuery.trim()
            if (query.isNotEmpty()) {
                result = result.filter {
                    it.title.contains(query, ignoreCase = true) ||
                        it.content.contains(query, ignoreCase = true)
                }
            }
            return result.sortedWith(
                compareByDescending<Note> { it.isPinned }.thenByDescending { it.modifiedAt },
            )
        }
}

class NoteListViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    initialCategoryFilter: UUID? = null,
) : ViewModel() {

    private val _state = MutableStateFlow(
        NoteListUiState(selectedCategoryId = initialCategoryFilter),
    )
    val state: StateFlow<NoteListUiState> = _state.asStateFlow()

    init {
        loadNotes()
        loadCategories()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val notes = getNotesUseCase.execute().sortedByDescending { it.modifiedAt }
                _state.update { it.copy(notes = notes, isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load notes",
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

    fun setSelectedCategory(id: UUID?) {
        _state.update { it.copy(selectedCategoryId = id) }
    }

    fun setSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun setShowArchivedAndCompleted(show: Boolean) {
        _state.update { it.copy(showArchivedAndCompleted = show) }
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val note = Note(title = title, content = content)
                saveNoteUseCase.execute(note)
                loadNotes()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to save note",
                    )
                }
            }
        }
    }

    fun deleteNote(id: UUID) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                deleteNoteUseCase.execute(id)
                _state.update { it ->
                    it.copy(
                        notes = it.notes.filter { n -> n.id != id },
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to delete note",
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
