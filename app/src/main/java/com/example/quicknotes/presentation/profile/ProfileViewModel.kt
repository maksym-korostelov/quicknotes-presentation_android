package com.example.quicknotes.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknotes.domain.entity.UserProfile
import com.example.quicknotes.domain.usecase.GetCategoriesUseCase
import com.example.quicknotes.domain.usecase.GetNotesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

data class ProfileUiState(
    val profile: UserProfile? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

class ProfileViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val notes = getNotesUseCase.execute()
                val categories = getCategoriesUseCase.execute()
                val joinedDate = Instant.now().minus(180, ChronoUnit.DAYS) // ~6 months ago, match iOS
                val profile = UserProfile(
                    displayName = "Maksym",
                    email = "maksym@quicknotes.app",
                    joinedDate = joinedDate,
                    notesCount = notes.size,
                    categoriesCount = categories.size,
                )
                _state.update { it.copy(profile = profile, isLoading = false) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load profile",
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
