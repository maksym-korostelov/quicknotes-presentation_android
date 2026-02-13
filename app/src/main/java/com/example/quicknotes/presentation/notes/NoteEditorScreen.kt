package com.example.quicknotes.presentation.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.ui.theme.AppColors
import com.example.quicknotes.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: NoteEditorViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    state.errorMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(msg) },
            confirmButton = { TextButton(onClick = { viewModel.clearError() }) { Text("OK") } },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.title.isNotBlank() || state.content.isNotBlank()) "Edit Note" else "New Note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.save() },
                        enabled = state.isValid && !state.isLoading,
                    ) {
                        Text("Save", style = AppTypography.headingSmall)
                    }
                },
            )
        },
    ) { padding ->
        LaunchedEffect(state.isSaved) {
            if (state.isSaved) onBack()
        }
        Column(
            Modifier
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
        ) {
            if (state.isLoading && state.title.isEmpty() && state.content.isEmpty()) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                }
            } else {
                Text("Title", style = AppTypography.captionLarge)
                Spacer(Modifier.height(4.dp))
                BasicTextField(
                    value = state.title,
                    onValueChange = { viewModel.setTitle(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textStyle = AppTypography.bodyLarge,
                    singleLine = false,
                    decorationBox = { inner ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                inner()
                            }
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Text("Content", style = AppTypography.captionLarge)
                Spacer(Modifier.height(4.dp))
                BasicTextField(
                    value = state.content,
                    onValueChange = { viewModel.setContent(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(12.dp),
                    textStyle = AppTypography.bodyLarge,
                    singleLine = false,
                    decorationBox = { inner ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                inner()
                            }
                        }
                    },
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "${state.content.length} characters",
                    style = AppTypography.captionSmall,
                    color = AppColors.textSecondary,
                )
                Spacer(Modifier.height(16.dp))
                Text("Category", style = AppTypography.captionLarge)
                Spacer(Modifier.height(4.dp))
                state.categories.forEach { category ->
                    val selected = state.selectedCategory?.id == category.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setSelectedCategory(if (selected) null else category) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant,
                        ),
                    ) {
                        Text(
                            text = category.name,
                            modifier = Modifier.padding(12.dp),
                            style = AppTypography.bodyLarge,
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                }
                Spacer(Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Pinned", modifier = Modifier.weight(1f), style = AppTypography.bodyLarge)
                        Switch(checked = state.isPinned, onCheckedChange = { viewModel.setPinned(it) })
                    }
                }
            }
        }
    }
}