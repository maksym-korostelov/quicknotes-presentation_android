package com.example.quicknotes.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val notes: List<Note> = emptyList(),
    val filteredNotes: List<Note> = emptyList(),
    val query: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class SearchViewModel(
    private val getNotesUseCase: GetNotesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    fun loadNotes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val notes = getNotesUseCase.execute()
                    .sortedWith(compareBy<Note> { !it.isPinned }.thenByDescending { it.modifiedAt })
                _state.update {
                    it.copy(
                        notes = notes,
                        filteredNotes = filterNotes(notes, it.query),
                        isLoading = false,
                    )
                }
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

    fun setQuery(query: String) {
        _state.update {
            it.copy(
                query = query,
                filteredNotes = filterNotes(it.notes, query),
            )
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun filterNotes(notes: List<Note>, query: String): List<Note> {
        val q = query.trim()
        if (q.isEmpty()) return notes
        return notes.filter {
            it.title.contains(q, ignoreCase = true) || it.content.contains(q, ignoreCase = true)
        }
    }
}
