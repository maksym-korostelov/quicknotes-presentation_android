package com.example.quicknotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.util.UUID
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quicknotes.core.AppDependencies
import com.example.quicknotes.core.ViewModelFactory
import com.example.quicknotes.domain.entity.Category
import com.example.quicknotes.presentation.categories.CategoriesScreen
import com.example.quicknotes.presentation.categories.CategoryListViewModel
import com.example.quicknotes.presentation.notes.NotesNavGraph
import com.example.quicknotes.presentation.profile.ProfileScreen
import com.example.quicknotes.presentation.settings.SettingsScreen
import com.example.quicknotes.ui.theme.QuickNotesTheme

class MainActivity : ComponentActivity() {

    private lateinit var appDependencies: AppDependencies
    private lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDependencies = AppDependencies(applicationContext)
        viewModelFactory = ViewModelFactory(appDependencies)
        enableEdgeToEdge()
        setContent {
            QuickNotesTheme {
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
                    when (selectedTab) {
                        0 -> NotesTab(
                            modifier = Modifier.padding(padding),
                            dependencies = appDependencies,
                            initialCategoryFilter = selectedCategoryFilter,
                            onFilterConsumed = { selectedCategoryFilter = null },
                        )
                        1 -> CategoriesTab(
                            modifier = Modifier.padding(padding),
                            viewModelFactory = viewModelFactory,
                            onCategoryClick = { category ->
                                selectedCategoryFilter = category.id
                                selectedTab = 0
                            },
                        )
                        2 -> SettingsScreen()
                        3 -> ProfileScreen()
                    }
                }
            }
        }
    }
}

private data class TabItem(val label: String, val icon: ImageVector)

@Composable
private fun NotesTab(
    modifier: Modifier,
    dependencies: AppDependencies,
    initialCategoryFilter: UUID?,
    onFilterConsumed: () -> Unit,
) {
    NotesNavGraph(
        modifier = modifier,
        dependencies = dependencies,
        initialCategoryFilter = initialCategoryFilter,
        onFilterConsumed = onFilterConsumed,
    )
}

@Composable
private fun CategoriesTab(
    modifier: Modifier,
    viewModelFactory: ViewModelProvider.Factory,
    onCategoryClick: (Category) -> Unit,
) {
    val viewModel: CategoryListViewModel = viewModel(factory = viewModelFactory)
    CategoriesScreen(
        viewModel = viewModel,
        onCategoryClick = onCategoryClick,
    )
}
