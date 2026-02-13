package com.example.quicknotes.data.repository

import com.example.quicknotes.data.SeedData
import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.domain.repository.CategoryRepository
import java.util.UUID
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryCategoryRepository(
    seedCategories: List<Category> = emptyList(),
) : CategoryRepository {

    private val mutex = Mutex()
    private val categories = if (seedCategories.isEmpty()) {
        SeedData.defaultCategories.toMutableList()
    } else {
        seedCategories.toMutableList()
    }

    override suspend fun fetchCategories(): List<Category> = mutex.withLock {
        categories.sortedBy { it.name.lowercase() }
    }

    override suspend fun fetchCategory(id: UUID): Category? = mutex.withLock {
        categories.find { it.id == id }
    }

    override suspend fun addCategory(category: Category) {
        mutex.withLock { categories.add(category) }
    }

    override suspend fun updateCategory(category: Category) {
        mutex.withLock {
            val index = categories.indexOfFirst { it.id == category.id }
            if (index >= 0) categories[index] = category
        }
    }

    override suspend fun deleteCategory(id: UUID) {
        mutex.withLock { categories.removeAll { it.id == id } }
    }
}
