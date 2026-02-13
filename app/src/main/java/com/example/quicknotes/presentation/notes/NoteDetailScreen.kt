package com.example.quicknotes.presentation.notes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quicknotes.domain.entity.Note
import com.example.quicknotes.ui.theme.AppColors
import com.example.quicknotes.ui.theme.AppTypography
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    viewModel: NoteDetailViewModel,
    onEdit: (Note) -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }

    state.errorMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(msg) },
            confirmButton = { androidx.compose.material3.TextButton(onClick = { viewModel.clearError() }) { Text("OK") } },
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Note") },
            text = { Text("Are you sure you want to delete this note?") },
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        viewModel.deleteNote()
                        showDeleteConfirm = false
                        onBack()
                    },
                ) { Text("Delete", style = AppTypography.bodyLarge, color = AppColors.textDestructive) }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel", style = AppTypography.bodyLarge, color = AppColors.textSecondary) }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    state.note?.let { note ->
                        IconButton(onClick = { viewModel.togglePin() }) {
                            Icon(
                                Icons.Filled.PushPin,
                                contentDescription = if (note.isPinned) "Unpin" else "Pin",
                                tint = if (note.isPinned) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                            )
                        }
                        IconButton(onClick = { viewModel.toggleArchive() }) {
                            Icon(
                                Icons.Filled.Archive,
                                contentDescription = if (note.isArchived) "Unarchive" else "Archive",
                                tint = if (note.isArchived) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                            )
                        }
                        IconButton(onClick = { viewModel.toggleCompleted() }) {
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = if (note.isCompleted) "Mark incomplete" else "Mark completed",
                                tint = if (note.isCompleted) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                            )
                        }
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                        IconButton(onClick = { onEdit(note) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        }
                    }
                },
            )
        },
    ) { padding ->
        if (state.isLoading && state.note == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            state.note?.let { note ->
                val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm").withZone(ZoneId.systemDefault())
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                ) {
                    Text(
                        text = note.title.ifEmpty { "(No title)" },
                        style = AppTypography.displayLarge,
                    )
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = note.content.ifEmpty { "No content" },
                        style = AppTypography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(24.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(16.dp))
                    Text("Created: ${formatter.format(note.createdAt)}", style = AppTypography.bodySmall)
                    Text("Modified: ${formatter.format(note.modifiedAt)}", style = AppTypography.bodySmall)
                    note.category?.let { cat ->
                        Spacer(Modifier.height(4.dp))
                        Text("Category: ${cat.name}", style = AppTypography.bodySmall)
                    }
                }
            }
        }
    }
}
