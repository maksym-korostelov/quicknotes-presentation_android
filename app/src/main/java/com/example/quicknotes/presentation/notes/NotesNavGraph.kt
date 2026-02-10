package com.example.quicknotes.presentation.notes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quicknotes.core.AppDependencies
import com.example.quicknotes.core.AppPreferences
import com.example.quicknotes.core.NoteDetailViewModelFactory
import com.example.quicknotes.core.NoteEditorViewModelFactory
import com.example.quicknotes.core.ViewModelFactory
import java.util.UUID

const val NOTES_LIST_ROUTE = "notesList"
const val NOTE_DETAIL_ROUTE = "noteDetail/{noteId}"
const val NOTE_EDITOR_NEW_ROUTE = "noteEditor"
const val NOTE_EDITOR_EDIT_ROUTE = "noteEditor/{noteId}"

fun noteDetailRoute(noteId: UUID) = "noteDetail/$noteId"
fun noteEditorRoute(noteId: UUID?) = if (noteId != null) "noteEditor/$noteId" else NOTE_EDITOR_NEW_ROUTE

@Composable
fun NotesNavGraph(
    modifier: Modifier = Modifier,
    dependencies: AppDependencies,
    initialCategoryFilter: UUID?,
    onFilterConsumed: () -> Unit,
    preferences: AppPreferences? = null,
) {
    val navController = rememberNavController()
    val viewModelFactory = remember(dependencies) { ViewModelFactory(dependencies) }

    NavHost(
        navController = navController,
        startDestination = NOTES_LIST_ROUTE,
        modifier = modifier,
    ) {
        composable(NOTES_LIST_ROUTE) {
            val noteListViewModel: NoteListViewModel = viewModel(factory = viewModelFactory)
            LaunchedEffect(Unit) {
                noteListViewModel.loadNotes()
            }
            LaunchedEffect(initialCategoryFilter) {
                initialCategoryFilter?.let { id ->
                    noteListViewModel.setSelectedCategory(id)
                    onFilterConsumed()
                }
            }
            preferences?.let { prefs ->
                LaunchedEffect(prefs.sortOrder) {
                    noteListViewModel.setSortOrder(prefs.sortOrder)
                }
            }
            NotesScreen(
                viewModel = noteListViewModel,
                onNoteClick = { note -> navController.navigate(noteDetailRoute(note.id)) },
                onAddNote = { navController.navigate(NOTE_EDITOR_NEW_ROUTE) },
            )
        }
        composable(NOTE_DETAIL_ROUTE) { backStackEntry ->
            val noteIdStr = backStackEntry.arguments?.getString("noteId")
            val noteId = noteIdStr?.let { id ->
                try {
                    UUID.fromString(id)
                } catch (_: Exception) {
                    null
                }
            }
            if (noteId == null) {
                Text("Invalid note")
            } else {
                val factory = NoteDetailViewModelFactory(dependencies = dependencies, noteId = noteId)
                val viewModel: NoteDetailViewModel = viewModel(backStackEntry, factory = factory)
                NoteDetailScreen(
                    viewModel = viewModel,
                    onEdit = { note -> navController.navigate(noteEditorRoute(note.id)) },
                    onBack = { navController.popBackStack() },
                )
            }
        }
        composable(NOTE_EDITOR_NEW_ROUTE) { backStackEntry ->
            val factory = NoteEditorViewModelFactory(
                dependencies = dependencies,
                savedStateHandle = backStackEntry.savedStateHandle,
            )
            val viewModel: NoteEditorViewModel = viewModel(backStackEntry, factory = factory)
            NoteEditorScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
        composable(NOTE_EDITOR_EDIT_ROUTE) { backStackEntry ->
            val factory = NoteEditorViewModelFactory(
                dependencies = dependencies,
                savedStateHandle = backStackEntry.savedStateHandle,
            )
            val viewModel: NoteEditorViewModel = viewModel(backStackEntry, factory = factory)
            NoteEditorScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
