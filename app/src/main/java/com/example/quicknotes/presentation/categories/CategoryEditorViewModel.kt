package com.example.quicknotes.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.domain.usecase.AddCategoryUseCase
import com.example.quicknotes.domain.usecase.UpdateCategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

data class CategoryEditorUiState(
    val name: String = "",
    val selectedIcon: String = "folder.fill",
    val selectedColorHex: String = "3B82F6",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
)

class CategoryEditorViewModel(
    private val existingCategory: Category?,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        CategoryEditorUiState(
            name = existingCategory?.name ?: "",
            selectedIcon = existingCategory?.icon ?: "folder.fill",
            selectedColorHex = existingCategory?.colorHex ?: "3B82F6",
        ),
    )
    val state: StateFlow<CategoryEditorUiState> = _state.asStateFlow()

    val isEditing: Boolean get() = existingCategory != null

    val isValid: Boolean
        get() = _state.value.name.trim().isNotEmpty()

    fun setName(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun setSelectedIcon(icon: String) {
        _state.update { it.copy(selectedIcon = icon) }
    }

    fun setSelectedColorHex(hex: String) {
        _state.update { it.copy(selectedColorHex = hex) }
    }

    fun save(onSaved: () -> Unit) {
        if (!isValid) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val s = _state.value
                val trimmedName = s.name.trim()
                if (existingCategory != null) {
                    val updated = Category(
                        id = existingCategory.id,
                        name = trimmedName,
                        icon = s.selectedIcon,
                        colorHex = s.selectedColorHex,
                        createdAt = existingCategory.createdAt,
                        modifiedAt = Instant.now(),
                    )
                    updateCategoryUseCase.execute(updated)
                } else {
                    val category = Category(
                        name = trimmedName,
                        icon = s.selectedIcon,
                        colorHex = s.selectedColorHex,
                    )
                    addCategoryUseCase.execute(category)
                }
                _state.update { it.copy(isLoading = false, isSaved = true) }
                onSaved()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to save category",
                    )
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    companion object {
        /** Icon id (stored in Category) to display label. */
        val availableIcons: List<Pair<String, String>> = listOf(
            "folder.fill" to "Folder",
            "briefcase.fill" to "Briefcase",
            "person.fill" to "Person",
            "lightbulb.fill" to "Lightbulb",
            "heart.fill" to "Heart",
            "star.fill" to "Star",
            "tag.fill" to "Tag",
            "book.fill" to "Book",
            "house.fill" to "House",
            "envelope.fill" to "Envelope",
            "camera.fill" to "Camera",
            "music.note" to "Music",
            "sportscourt.fill" to "Sport",
            "cart.fill" to "Cart",
            "gift.fill" to "Gift",
        )

        val availableColors: List<Pair<String, String>> = listOf(
            "3B82F6" to "Blue",
            "10B981" to "Green",
            "F59E0B" to "Amber",
            "EF4444" to "Red",
            "8B5CF6" to "Purple",
            "EC4899" to "Pink",
            "6366F1" to "Indigo",
            "14B8A6" to "Teal",
            "F97316" to "Orange",
            "6B7280" to "Gray",
        )
    }
}
