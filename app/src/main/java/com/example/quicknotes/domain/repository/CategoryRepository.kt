package com.example.quicknotes.domain.repository

import com.example.quicknotes.domain.entity.Category
import java.util.UUID

/**
 * Defines the contract for category data access.
 */
interface CategoryRepository {

    /** Fetches all available categories (sorted by name). */
    suspend fun fetchCategories(): List<Category>

    /** Fetches a single category by its identifier. */
    suspend fun fetchCategory(id: UUID): Category?

    /** Adds a new category. */
    suspend fun addCategory(category: Category)

    /** Updates an existing category. */
    suspend fun updateCategory(category: Category)

    /** Deletes a category by its identifier. */
    suspend fun deleteCategory(id: UUID)
}
