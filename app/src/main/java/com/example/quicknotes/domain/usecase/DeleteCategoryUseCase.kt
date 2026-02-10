package com.example.quicknotes.domain.usecase

import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.domain.repository.CategoryRepository
import java.time.Instant
import java.util.UUID
/**
 * Deletes a category and unassigns it from all notes that used it.
 */
class DeleteCategoryUseCase constructor(
    private val categoryRepository: CategoryRepository,
    private val getNotesUseCase: GetNotesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
) {
    suspend fun execute(categoryId: UUID) {
        val notes = getNotesUseCase.execute()
        notes.filter { it.category?.id == categoryId }.forEach { note ->
            val updated = note.copy(
                category = null,
                modifiedAt = Instant.now(),
            )
            saveNoteUseCase.execute(updated)
        }
        categoryRepository.deleteCategory(categoryId)
    }
}
