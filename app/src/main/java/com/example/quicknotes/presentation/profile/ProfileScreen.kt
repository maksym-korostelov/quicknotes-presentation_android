package com.example.quicknotes.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.quicknotes.ui.theme.AppColors
import com.example.quicknotes.ui.theme.AppTypography
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profile") })
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (state.isLoading && state.profile == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            } else {
                Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // Avatar section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        modifier = Modifier.size(AppTypography.iconHeroXXLarge.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = state.profile?.displayName ?: "User",
                        style = AppTypography.displayMedium,
                    )
                    Text(
                        text = state.profile?.email ?: "",
                        style = AppTypography.bodyMedium,
                        color = AppColors.textSecondary,
                    )
                    state.profile?.joinedDate?.let { joined ->
                        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy").withZone(ZoneId.systemDefault())
                        Text(
                            text = "Member since ${formatter.format(joined)}",
                            style = AppTypography.captionLarge,
                            color = AppColors.textTertiary,
                        )
                    }
                }

                // Activity section
                Text(
                    "Activity",
                    style = AppTypography.headingMedium,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    ProfileStatCard(
                        title = "Notes",
                        value = "${state.profile?.notesCount ?: 0}",
                        icon = Icons.Filled.Description,
                        modifier = Modifier.weight(1f),
                    )
                    ProfileStatCard(
                        title = "Categories",
                        value = "${state.profile?.categoriesCount ?: 0}",
                        icon = Icons.Filled.Folder,
                        modifier = Modifier.weight(1f),
                    )
                }

                // Account section
                Text(
                    "Account",
                    style = AppTypography.headingMedium,
                )
                val (destructiveStyle, destructiveColor) = AppTypography.bodyLargeDestructive()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.size(12.dp))
                            Text("Edit Profile", style = AppTypography.bodyLarge)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Filled.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.size(12.dp))
                            Text("Sign Out", style = destructiveStyle, color = destructiveColor)
                        }
                    }
                }
            }
        }
    }
    }
}

@Composable
fun ProfileStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                value,
                style = AppTypography.headingLarge,
            )
            Text(
                title,
                style = AppTypography.bodySmall,
                color = AppColors.textSecondary,
            )
        }
    }
}
