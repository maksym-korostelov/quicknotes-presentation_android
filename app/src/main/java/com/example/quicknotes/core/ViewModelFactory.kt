package com.example.quicknotes.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quicknotes.presentation.categories.CategoryListViewModel
import com.example.quicknotes.presentation.notes.NoteListViewModel
import com.example.quicknotes.presentation.search.SearchViewModel

class ViewModelFactory(
    private val dependencies: AppDependencies,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NoteListViewModel::class.java) ->
                dependencies.createNoteListViewModel() as T
            modelClass.isAssignableFrom(CategoryListViewModel::class.java) ->
                dependencies.createCategoryListViewModel() as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                dependencies.createSearchViewModel() as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}
