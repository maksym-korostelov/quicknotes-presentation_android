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
            Note(title = "Welcome to QuickNotes", content = "First note. Swipe, tap, ignore the other 83 apps. You're in charge here.", category = null, isPinned = true),
            Note(title = "Shopping List", content = "Milk, bread, more coffee. Also that thing from the other aisle. You know the one.", category = personal),
            Note(title = "Meeting Notes", content = "Agenda: 3 items. Discussion: 47 tangents. Decisions made: zero. Snacks: good.", category = work),
            Note(title = "Project Alpha ideas", content = "MVP scope: everything. Timeline: soon. 'Soon' is not a date. We know.", category = ideas),
            Note(title = "Weekly standup", content = "• Yesterday: meetings\n• Today: more meetings\n• Blockers: need time to actually do work", category = work),
            Note(title = "Books to read", content = "Stack by the bed. One bookmarked at page 12 since March. It's fine. I'm fine.", category = personal),
            Note(title = "Feature brainstorm", content = "Voice notes (so I can forget to listen), cloud sync (lose things everywhere), AI summary (not read my own notes).", category = ideas),
            Note(title = "Vacation packing", content = "Phone, charger, backup charger. If I forget pants I'll buy some there. Priorities.", category = personal),
            Note(title = "Sprint retrospective", content = "Went well: nobody cried. To improve: everything else. Action items: same as last retro. We'll get to them.", category = work),
            Note(title = "App name ideas", content = "Noteworthy (taken), JotBot (sounds like a vacuum), Noteify (sounds like notify). Back to the whiteboard.", category = ideas),
        )
    }
}
