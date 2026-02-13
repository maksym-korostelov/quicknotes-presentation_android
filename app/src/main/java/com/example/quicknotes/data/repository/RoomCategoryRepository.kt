package com.example.quicknotes.data.repository

import com.example.quicknotes.data.local.dao.CategoryDao
import com.example.quicknotes.data.local.entity.CategoryEntity
import com.example.quicknotes.data.local.toDomain
import com.example.quicknotes.data.local.toEntity
import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.domain.repository.CategoryRepository
import java.util.UUID

class RoomCategoryRepository(
    private val dao: CategoryDao,
) : CategoryRepository {

    override suspend fun fetchCategories(): List<Category> =
        dao.getAll().map { it.toDomain() }

    override suspend fun fetchCategory(id: UUID): Category? =
        dao.getById(id.toString())?.toDomain()

    override suspend fun addCategory(category: Category) {
        dao.insert(category.toEntity())
    }

    override suspend fun updateCategory(category: Category) {
        dao.update(category.toEntity())
    }

    override suspend fun deleteCategory(id: UUID) {
        dao.deleteById(id.toString())
    }
}
