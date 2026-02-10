package com.example.quicknotes.core

import android.content.Context
import com.example.quicknotes.data.SeedData
import com.example.quicknotes.data.local.AppDatabase
import com.example.quicknotes.data.repository.InMemoryCategoryRepository
import com.example.quicknotes.data.repository.InMemoryNoteRepository
import com.example.quicknotes.data.repository.RoomCategoryRepository
import com.example.quicknotes.data.repository.RoomNoteRepository
import com.example.quicknotes.domain.repository.CategoryRepository
import com.example.quicknotes.domain.repository.NoteRepository
import com.example.quicknotes.domain.usecase.AddCategoryUseCase
import com.example.quicknotes.domain.usecase.DeleteCategoryUseCase
import com.example.quicknotes.domain.usecase.DeleteNoteUseCase
import com.example.quicknotes.domain.usecase.GetCategoriesUseCase
import com.example.quicknotes.domain.usecase.GetNoteUseCase
import com.example.quicknotes.domain.usecase.GetNotesUseCase
import com.example.quicknotes.domain.usecase.SaveNoteUseCase
import com.example.quicknotes.domain.usecase.UpdateCategoryUseCase
import com.example.quicknotes.presentation.categories.CategoryEditorViewModel
import com.example.quicknotes.presentation.categories.CategoryListViewModel
import com.example.quicknotes.presentation.notes.NoteDetailViewModel
import com.example.quicknotes.presentation.notes.NoteEditorViewModel
import com.example.quicknotes.presentation.notes.NoteListViewModel
import com.example.quicknotes.presentation.profile.ProfileViewModel
import com.example.quicknotes.presentation.search.SearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import com.example.quicknotes.domain.entity.Category
import java.util.UUID

/**
 * App-level dependency container for building view models.
 * Uses Room for persistence when context is provided.
 */
class AppDependencies(
    private val context: Context? = null,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database by lazy {
        context?.let { AppDatabase.create(it) }
    }

    private val categoryRepository: CategoryRepository by lazy {
        database?.let { db ->
            RoomCategoryRepository(db.categoryDao())
        } ?: InMemoryCategoryRepository(seedCategories = SeedData.defaultCategories)
    }

    private val noteRepository: NoteRepository by lazy {
        database?.let { db ->
            RoomNoteRepository(db.noteDao(), db.categoryDao())
        } ?: InMemoryNoteRepository(
            seedNotes = SeedData.defaultNotes(SeedData.defaultCategories),
        )
    }

    private val _getNotesUseCase by lazy { GetNotesUseCase(noteRepository) }
    private val _getNoteUseCase by lazy { GetNoteUseCase(noteRepository) }
    private val _saveNoteUseCase by lazy { SaveNoteUseCase(noteRepository) }
    private val _deleteNoteUseCase by lazy { DeleteNoteUseCase(noteRepository) }
    private val _getCategoriesUseCase by lazy { GetCategoriesUseCase(categoryRepository) }
    private val _addCategoryUseCase by lazy { AddCategoryUseCase(categoryRepository) }
    private val _updateCategoryUseCase by lazy { UpdateCategoryUseCase(categoryRepository) }
    private val _deleteCategoryUseCase by lazy {
        DeleteCategoryUseCase(
            categoryRepository = categoryRepository,
            getNotesUseCase = _getNotesUseCase,
            saveNoteUseCase = _saveNoteUseCase,
        )
    }

    val getNotesUseCase get() = _getNotesUseCase
    val getNoteUseCase get() = _getNoteUseCase
    val saveNoteUseCase get() = _saveNoteUseCase
    val deleteNoteUseCase get() = _deleteNoteUseCase
    val getCategoriesUseCase get() = _getCategoriesUseCase
    val addCategoryUseCase get() = _addCategoryUseCase
    val updateCategoryUseCase get() = _updateCategoryUseCase
    val deleteCategoryUseCase get() = _deleteCategoryUseCase

    init {
        if (database != null) {
            scope.launch { seedIfNeeded() }
        }
    }

    /**
     * Seeds default categories and notes if the store is empty.
     */
    suspend fun seedIfNeeded() {
        if (database == null) return
        try {
            val notes = getNotesUseCase.execute()
            if (notes.isNotEmpty()) return
            for (category in SeedData.defaultCategories) {
                addCategoryUseCase.execute(category)
            }
            val categories = getCategoriesUseCase.execute()
            for (note in SeedData.defaultNotes(categories)) {
                saveNoteUseCase.execute(note)
            }
        } catch (_: Exception) {
            // Ignore (e.g. already seeded or DB error)
        }
    }

    fun createNoteListViewModel(initialCategoryFilter: UUID? = null) = NoteListViewModel(
        getNotesUseCase = getNotesUseCase,
        getCategoriesUseCase = getCategoriesUseCase,
        saveNoteUseCase = saveNoteUseCase,
        deleteNoteUseCase = deleteNoteUseCase,
        initialCategoryFilter = initialCategoryFilter,
    )

    fun createCategoryListViewModel() = CategoryListViewModel(
        getCategoriesUseCase = getCategoriesUseCase,
        addCategoryUseCase = addCategoryUseCase,
        updateCategoryUseCase = updateCategoryUseCase,
        deleteCategoryUseCase = deleteCategoryUseCase,
    )

    fun createNoteDetailViewModel(noteId: UUID) = NoteDetailViewModel(
        noteId = noteId,
        getNoteUseCase = getNoteUseCase,
        deleteNoteUseCase = deleteNoteUseCase,
        saveNoteUseCase = saveNoteUseCase,
    )

    fun createNoteEditorViewModel(noteId: UUID?) = NoteEditorViewModel(
        existingNoteId = noteId,
        getNoteUseCase = getNoteUseCase,
        getCategoriesUseCase = getCategoriesUseCase,
        saveNoteUseCase = saveNoteUseCase,
    )

    fun createSearchViewModel() = SearchViewModel(getNotesUseCase = getNotesUseCase)

    fun createProfileViewModel() = ProfileViewModel(
        getNotesUseCase = getNotesUseCase,
        getCategoriesUseCase = getCategoriesUseCase,
    )

    fun createCategoryEditorViewModel(existingCategory: Category?) = CategoryEditorViewModel(
        existingCategory = existingCategory,
        addCategoryUseCase = addCategoryUseCase,
        updateCategoryUseCase = updateCategoryUseCase,
    )
}
