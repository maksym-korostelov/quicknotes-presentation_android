package com.example.quicknotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.UUID
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quicknotes.core.AppDependencies
import com.example.quicknotes.core.AppPreferences
import com.example.quicknotes.core.ViewModelFactory
import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.presentation.categories.CategoriesScreen
import com.example.quicknotes.presentation.categories.CategoryEditorViewModel
import com.example.quicknotes.presentation.categories.CategoryEditorScreen
import com.example.quicknotes.presentation.categories.CategoryListViewModel
import com.example.quicknotes.presentation.notes.NotesNavGraph
import com.example.quicknotes.presentation.onboarding.OnboardingScreen
import com.example.quicknotes.presentation.profile.ProfileScreen
import com.example.quicknotes.presentation.profile.ProfileViewModel
import com.example.quicknotes.presentation.search.SearchScreen
import com.example.quicknotes.presentation.search.SearchTab
import com.example.quicknotes.presentation.search.SearchViewModel
import com.example.quicknotes.presentation.settings.SettingsScreen
import com.example.quicknotes.ui.theme.QuickNotesTheme

class MainActivity : ComponentActivity() {

    private lateinit var appDependencies: AppDependencies
    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var preferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDependencies = AppDependencies(applicationContext)
        viewModelFactory = ViewModelFactory(appDependencies)
        preferences = AppPreferences(applicationContext)
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(preferences.isDarkModeEnabled) }
            QuickNotesTheme(darkTheme = darkTheme) {
                var showOnboarding by remember { mutableStateOf(!preferences.hasCompletedOnboarding) }
                if (showOnboarding) {
                    OnboardingScreen(onComplete = {
                        preferences.hasCompletedOnboarding = true
                        showOnboarding = false
                    })
                } else {
                    MainContent(
                        appDependencies = appDependencies,
                        viewModelFactory = viewModelFactory,
                        preferences = preferences,
                        darkTheme = darkTheme,
                        onDarkModeChange = {
                            preferences.isDarkModeEnabled = it
                            darkTheme = it
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    appDependencies: AppDependencies,
    viewModelFactory: ViewModelProvider.Factory,
    preferences: AppPreferences,
    darkTheme: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategoryFilter by remember { mutableStateOf<UUID?>(null) }
    Scaffold(
        bottomBar = {
            NavigationBar {
                listOf(
                    TabItem("Notes", Icons.Filled.List),
                    TabItem("Categories", Icons.Filled.Folder),
                    TabItem("Settings", Icons.Filled.Settings),
                    TabItem("Profile", Icons.Filled.Person),
                    TabItem("Search", Icons.Filled.Search),
                ).forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                    )
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(padding),
        ) {
            when (selectedTab) {
            0 -> NotesTab(
                modifier = Modifier.fillMaxSize(),
                dependencies = appDependencies,
                preferences = preferences,
                initialCategoryFilter = selectedCategoryFilter,
                onFilterConsumed = { selectedCategoryFilter = null },
            )
            1 -> CategoriesTab(
                modifier = Modifier.fillMaxSize(),
                viewModelFactory = viewModelFactory,
                dependencies = appDependencies,
                onCategoryClick = { category ->
                    selectedCategoryFilter = category.id
                    selectedTab = 0
                },
            )
            2 -> Box(modifier = Modifier.fillMaxSize()) {
                SettingsScreen(
                    preferences = preferences,
                    onDarkModeChange = onDarkModeChange,
                )
            }
            3 -> Box(modifier = Modifier.fillMaxSize()) {
                val profileViewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
                ProfileScreen(viewModel = profileViewModel)
            }
            4 -> SearchTab(
                modifier = Modifier.fillMaxSize(),
                viewModelFactory = viewModelFactory,
                dependencies = appDependencies,
            )
            }
        }
    }
}

private data class TabItem(val label: String, val icon: ImageVector)

@Composable
private fun NotesTab(
    modifier: Modifier,
    dependencies: AppDependencies,
    preferences: AppPreferences,
    initialCategoryFilter: UUID?,
    onFilterConsumed: () -> Unit,
) {
    NotesNavGraph(
        modifier = modifier,
        dependencies = dependencies,
        initialCategoryFilter = initialCategoryFilter,
        onFilterConsumed = onFilterConsumed,
        preferences = preferences,
    )
}

@Composable
private fun CategoriesTab(
    modifier: Modifier,
    viewModelFactory: ViewModelProvider.Factory,
    dependencies: AppDependencies,
    onCategoryClick: (Category) -> Unit,
) {
    val listViewModel: CategoryListViewModel = viewModel(factory = viewModelFactory)
    var showAddCategory by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }

    when {
        showAddCategory || categoryToEdit != null -> {
            val existing = categoryToEdit
            key(if (showAddCategory) "new" else existing!!.id.toString()) {
                val factory = com.example.quicknotes.core.CategoryEditorViewModelFactory(dependencies, existing)
                val editorViewModel: CategoryEditorViewModel = viewModel(factory = factory)
                CategoryEditorScreen(
                    viewModel = editorViewModel,
                    showCancelButton = true,
                    onCancel = { showAddCategory = false; categoryToEdit = null },
                    onSaved = {
                        showAddCategory = false
                        categoryToEdit = null
                        listViewModel.loadCategories()
                    },
                )
            }
        }
        else -> {
            CategoriesScreen(
                viewModel = listViewModel,
                onCategoryClick = onCategoryClick,
                onAddCategory = { showAddCategory = true },
                onEditCategory = { categoryToEdit = it },
            )
        }
    }
}
