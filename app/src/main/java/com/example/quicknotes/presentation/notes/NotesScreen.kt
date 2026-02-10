package com.example.quicknotes.presentation.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.quicknotes.domain.entity.Note
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    viewModel: NoteListViewModel,
    onNoteClick: (Note) -> Unit,
    onAddNote: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    var showFilterMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes") },
                actions = {
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(
                            Icons.Filled.FilterList,
                            contentDescription = "Filter",
                            tint = if (state.showArchivedAndCompleted) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (state.showArchivedAndCompleted)
                                        "Hide archived & completed"
                                    else
                                        "Show archived & completed",
                                )
                            },
                            onClick = {
                                viewModel.setShowArchivedAndCompleted(!state.showArchivedAndCompleted)
                                showFilterMenu = false
                            },
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Icon(Icons.Default.Add, contentDescription = "Add note")
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (state.isLoading && state.notes.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            } else if (state.filteredNotes.isEmpty()) {
                Text(
                    text = if (state.showArchivedAndCompleted)
                        "No notes yet. Tap + to create one."
                    else
                        "No notes to show. Tap the filter icon and choose \"Show archived & completed\" to see all.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp),
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = state.filteredNotes,
                        key = { it.id.toString() },
                    ) { note ->
                        NoteItem(
                            note = note,
                            onClick = { onNoteClick(note) },
                        )
                    }
                }
            }
        }
    }
}

private val SideLineWidth = 4.dp
private val ArchivedLineColor = Color(0xFF9E9E9E)
private val CompletedLineColor = Color(0xFF4CAF50)

@Composable
private fun NoteItem(
    note: Note,
    onClick: () -> Unit,
) {
    val formatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")
        .withZone(ZoneId.systemDefault())
    val dateStr = formatter.format(note.modifiedAt)
    val showSideLine = note.isArchived || note.isCompleted
    val sideLineColor = when {
        note.isCompleted -> CompletedLineColor
        note.isArchived -> ArchivedLineColor
        else -> Color.Transparent
    }
    val textDecoration = if (note.isCompleted) TextDecoration.LineThrough else TextDecoration.None

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            if (showSideLine) {
                Box(
                    modifier = Modifier
                        .width(SideLineWidth)
                        .fillMaxHeight()
                        .background(sideLineColor),
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = note.title.ifEmpty { "(No title)" },
                        style = MaterialTheme.typography.titleMedium.copy(textDecoration = textDecoration),
                        modifier = Modifier.weight(1f),
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (note.isArchived) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = ArchivedLineColor.copy(alpha = 0.2f),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = ArchivedLineColor,
                                    )
                                    Text(
                                        "Archived",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ArchivedLineColor,
                                    )
                                }
                            }
                        }
                        if (note.isCompleted) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = CompletedLineColor.copy(alpha = 0.25f),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Icon(
                                        Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = CompletedLineColor,
                                    )
                                    Text(
                                        "Completed",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CompletedLineColor,
                                    )
                                }
                            }
                        }
                    }
                }
                if (note.content.isNotEmpty()) {
                    Text(
                        text = note.content.take(120).let { if (note.content.length > 120) "$it..." else it },
                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = textDecoration),
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}
