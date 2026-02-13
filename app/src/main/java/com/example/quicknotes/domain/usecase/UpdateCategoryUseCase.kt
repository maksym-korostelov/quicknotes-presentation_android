package com.example.quicknotes.domain.usecase

import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.domain.repository.CategoryRepository
class UpdateCategoryUseCase constructor(
    private val repository: CategoryRepository,
) {
    suspend fun execute(category: Category) = repository.updateCategory(category)
}
