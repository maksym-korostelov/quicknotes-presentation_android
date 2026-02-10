package com.example.quicknotes.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quicknotes.core.AppPreferences
import com.example.quicknotes.presentation.about.AboutScreen
import com.example.quicknotes.presentation.help.HelpScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferences: AppPreferences,
    onDarkModeChange: (Boolean) -> Unit = {},
) {
    var showAbout by remember { mutableStateOf(false) }
    var showHelp by remember { mutableStateOf(false) }
    var darkMode by remember(preferences) { mutableStateOf(preferences.isDarkModeEnabled) }
    var notifications by remember(preferences) { mutableStateOf(preferences.isNotificationsEnabled) }
    var sortOrder by remember(preferences) { mutableStateOf(preferences.sortOrder) }

    when {
        showAbout -> AboutScreen(onBack = { showAbout = false })
        showHelp -> HelpScreen(onBack = { showHelp = false })
        else -> {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Settings") })
                },
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                ) {
                    // Appearance
                    SectionTitle("Appearance")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                        Switch(
                            checked = darkMode,
                            onCheckedChange = {
                                darkMode = it
                                preferences.isDarkModeEnabled = it
                                onDarkModeChange(it)
                            },
                        )
                    }
                    SectionFooter("Customize how QuickNotes looks")
                    HorizontalDivider()

                    // Notifications
                    SectionTitle("Notifications")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Enable Notifications", style = MaterialTheme.typography.bodyLarge)
                        Switch(
                            checked = notifications,
                            onCheckedChange = {
                                notifications = it
                                preferences.isNotificationsEnabled = it
                            },
                        )
                    }
                    SectionFooter("Manage how QuickNotes sends you notifications")
                    HorizontalDivider()

                    // Sort Order
                    SectionTitle("Sort Order")
                    AppPreferences.SortOrder.all.forEach { order ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { sortOrder = order; preferences.sortOrder = order }
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(order, style = MaterialTheme.typography.bodyLarge)
                            if (sortOrder == order) {
                                Text("✓", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                    SectionFooter("Applies to the order of notes in the Notes tab")
                    HorizontalDivider()

                    // Data (placeholders)
                    SectionTitle("Data")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Export Notes", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Import Notes", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Delete All Notes", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                    }
                    HorizontalDivider()

                    // About
                    SectionTitle("About")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAbout = true }
                            .padding(vertical = 12.dp),
                    ) {
                        Text("About QuickNotes", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showHelp = true }
                            .padding(vertical = 12.dp),
                    ) {
                        Text("Help", style = MaterialTheme.typography.bodyLarge)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Version", style = MaterialTheme.typography.bodyLarge)
                        Text("1.0.0", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Build", style = MaterialTheme.typography.bodyLarge)
                        Text("2025.1", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
    )
}

@Composable
private fun SectionFooter(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}
