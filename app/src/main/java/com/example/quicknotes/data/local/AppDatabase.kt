package com.example.quicknotes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quicknotes.data.local.dao.CategoryDao
import com.example.quicknotes.data.local.dao.NoteDao
import com.example.quicknotes.data.local.entity.CategoryEntity
import com.example.quicknotes.data.local.entity.NoteEntity

@Database(
    entities = [NoteEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private const val DATABASE_NAME = "quicknotes.db"

        fun create(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }
}
