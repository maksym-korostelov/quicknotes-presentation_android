package com.example.quicknotes.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import com.example.quicknotes.core.AppDependencies
import com.example.quicknotes.core.NoteDetailViewModelFactory
import com.example.quicknotes.core.NoteEditorViewModelFactory
import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.presentation.notes.NoteDetailScreen
import com.example.quicknotes.presentation.notes.NoteDetailViewModel
import com.example.quicknotes.presentation.notes.NoteEditorScreen
import com.example.quicknotes.presentation.notes.NoteEditorViewModel
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    dependencies: AppDependencies,
    onNavigateToNoteDetail: (UUID) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }

    state.errorMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(msg) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) { Text("OK") }
            },
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Search") })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.setQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                placeholder = { Text("Search notes by title or content") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                    )
                },
                singleLine = true,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
            ) {
                when {
                    state.isLoading && state.notes.isEmpty() -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    state.filteredNotes.isEmpty() -> {
                        val emptyMessage = when {
                            state.notes.isEmpty() && state.query.isBlank() ->
                                "No notes yet. Create your first note from the Notes tab."
                            state.query.isNotBlank() ->
                                "No notes match \"${state.query.trim()}\"."
                            else ->
                                "Enter a search term to find notes."
                        }
                        Text(
                            text = emptyMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                        )
                    }
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(
                                items = state.filteredNotes,
                                key = { it.id.toString() },
                            ) { note ->
                                SearchNoteItem(
                                    note = note,
                                    onClick = { onNavigateToNoteDetail(note.id) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchNoteItem(
    note: Note,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = note.title.ifEmpty { "(No title)" },
                style = MaterialTheme.typography.titleMedium,
            )
            if (note.content.isNotEmpty()) {
                Text(
                    text = note.content.take(120).let { if (note.content.length > 120) "$it..." else it },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            note.category?.let { cat ->
                Text(
                    text = cat.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
fun SearchTab(
    modifier: Modifier,
    viewModelFactory: ViewModelProvider.Factory,
    dependencies: AppDependencies,
) {
    var noteIdToDetail by remember { mutableStateOf<UUID?>(null) }
    var noteIdToEdit by remember { mutableStateOf<UUID?>(null) }
    val searchViewModel: SearchViewModel = viewModel(factory = viewModelFactory)

    val currentEditId = noteIdToEdit
    val currentDetailId = noteIdToDetail

    when {
        currentEditId != null -> {
            val factory = NoteEditorViewModelFactory(
                dependencies = dependencies,
                noteId = currentEditId,
            )
            val editorViewModel: NoteEditorViewModel = viewModel(factory = factory)
            NoteEditorScreen(
                viewModel = editorViewModel,
                onBack = { noteIdToEdit = null },
            )
        }
        currentDetailId != null -> {
            val factory = NoteDetailViewModelFactory(dependencies = dependencies, noteId = currentDetailId)
            val detailViewModel: NoteDetailViewModel = viewModel(factory = factory)
            NoteDetailScreen(
                viewModel = detailViewModel,
                onEdit = { note -> noteIdToDetail = null; noteIdToEdit = note.id },
                onBack = { noteIdToDetail = null },
            )
        }
        else -> {
            SearchScreen(
                modifier = modifier,
                viewModel = searchViewModel,
                dependencies = dependencies,
                onNavigateToNoteDetail = { noteIdToDetail = it },
            )
        }
    }
}
