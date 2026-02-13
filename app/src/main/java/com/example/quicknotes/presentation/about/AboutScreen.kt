package com.example.quicknotes.presentation.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quicknotes.ui.theme.AppColors
import com.example.quicknotes.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
                navigationIcon = {
                    androidx.compose.material3.IconButton(onClick = onBack) {
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Icon(
                Icons.Filled.Description,
                contentDescription = null,
                modifier = Modifier.size(AppTypography.iconHeroLarge.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "QuickNotes",
                style = AppTypography.displayLarge,
            )
            Text(
                text = "Capture ideas. Stay organized.",
                style = AppTypography.bodyMedium,
                color = AppColors.textSecondary,
                textAlign = TextAlign.Center,
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                color = AppColors.backgroundSecondary,
            ) {
                val (valueStyle, valueColor) = AppTypography.bodyMediumValue()
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text("Version", style = AppTypography.headingSmall)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Version", style = AppTypography.bodyLarge)
                        Text("1.0.0", style = valueStyle, color = valueColor)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Build", style = AppTypography.bodyLarge)
                        Text("2025.1", style = valueStyle, color = valueColor)
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "Credits",
                    style = AppTypography.headingMedium,
                )
                Text(
                    "QuickNotes is built with Kotlin and Jetpack Compose. Icons and visuals are designed to keep your notes simple and accessible.",
                    style = AppTypography.bodyLarge,
                    color = AppColors.textSecondary,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Legal", style = AppTypography.headingMedium)
                val (actionStyle, actionColor) = AppTypography.bodyLargeAction()
                androidx.compose.material3.TextButton(
                    onClick = { uriHandler.openUri("https://example.com/privacy") },
                ) { Text("Privacy Policy", style = actionStyle, color = actionColor) }
                androidx.compose.material3.TextButton(
                    onClick = { uriHandler.openUri("https://example.com/terms") },
                ) { Text("Terms of Service", style = actionStyle, color = actionColor) }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Made with ❤️ using Jetpack Compose",
                style = AppTypography.bodySmall,
                color = AppColors.textTertiary,
            )
        }
    }
}
