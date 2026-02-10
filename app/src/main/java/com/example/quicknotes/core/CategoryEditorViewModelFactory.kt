package com.example.quicknotes.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.presentation.categories.CategoryEditorViewModel

class CategoryEditorViewModelFactory(
    private val dependencies: AppDependencies,
    private val existingCategory: Category?,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return dependencies.createCategoryEditorViewModel(existingCategory) as T
    }
}
