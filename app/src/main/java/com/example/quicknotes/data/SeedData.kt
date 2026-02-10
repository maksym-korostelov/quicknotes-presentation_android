package com.example.quicknotes.data

import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.domain.entity.Note

/**
 * Shared seed data for in-memory repositories.
 */
object SeedData {

    val defaultCategories: List<Category> = listOf(
        Category(name = "Work", icon = "briefcase.fill", colorHex = "F59E0B"),
        Category(name = "Personal", icon = "person.fill", colorHex = "3B82F6"),
        Category(name = "Ideas", icon = "lightbulb.fill", colorHex = "10B981"),
    )

    fun defaultNotes(categories: List<Category>): List<Note> {
        val work = categories.find { it.name == "Work" }
        val personal = categories.find { it.name == "Personal" }
        val ideas = categories.find { it.name == "Ideas" }
        return listOf(
            Note(title = "Welcome to QuickNotes", content = "This is your first note. Tap + to create more!", category = null, isPinned = true),
            Note(title = "Shopping List", content = "Milk, Eggs, Bread, Butter", category = personal),
            Note(title = "Meeting Notes", content = "Discuss Q4 roadmap with the team.", category = work),
            Note(title = "Project Alpha ideas", content = "Consider dark mode, widgets, and offline sync.", category = ideas),
            Note(title = "Weekly standup", content = "• Backend API on track\n• Design review Thursday\n• Deploy to staging Friday", category = work),
            Note(title = "Books to read", content = "1. Deep Work\n2. Atomic Habits\n3. The Pragmatic Programmer", category = personal),
            Note(title = "Feature brainstorm", content = "Tags, reminders, rich text, export to PDF.", category = ideas),
            Note(title = "Vacation packing", content = "Passport, charger, adapters, meds, sunscreen.", category = personal),
            Note(title = "Sprint retrospective", content = "What went well: shipping on time. Improve: earlier QA involvement.", category = work),
            Note(title = "App name ideas", content = "NoteFlow, QuickJot, MemoBox, Scribble.", category = ideas),
        )
    }
}
