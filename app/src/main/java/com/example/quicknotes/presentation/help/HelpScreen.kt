package com.example.quicknotes.presentation.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "How to use QuickNotes",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    "QuickNotes helps you capture and organize your notes with categories and search. Here's a quick guide to get the most out of the app.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            HelpSection(
                title = "Creating notes",
                icon = Icons.Filled.Create,
                text = "Tap the + button on the Notes tab to create a new note. Add a title and content, and optionally assign a category. Tap Save to keep your note.",
            )
            HelpSection(
                title = "Organizing with categories",
                icon = Icons.Filled.Folder,
                text = "Use the Categories tab to see all your categories. Tap a category to jump to the Notes tab with that filter applied. You can filter notes by category using the picker at the top of the Notes list.",
            )
            HelpSection(
                title = "Searching notes",
                icon = Icons.Filled.Search,
                text = "Tap the Search tab and type in the search field. Notes are searched by title and content. Clear the search to see all notes again.",
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Tips",
                    style = MaterialTheme.typography.titleMedium,
                )
                listOf(
                    "Swipe or use the menu on a note in the list to delete it.",
                    "Use the Sort Order setting to sort notes by date or title.",
                    "Tap a note to view it in full; tap Edit to make changes.",
                ).forEachIndexed { index, tip ->
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            "${index + 1}.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            tip,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HelpSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
