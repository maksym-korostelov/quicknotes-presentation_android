package com.example.quicknotes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Semantic color tokens for QuickNotes Android app.
 * Provides consistent color usage across the application.
 */
object AppColors {
    
    // Text colors
    val textPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface
    
    val textSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
    
    val textTertiary: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    
    val textDestructive: Color
        @Composable get() = MaterialTheme.colorScheme.error
    
    val textAction: Color
        @Composable get() = MaterialTheme.colorScheme.primary
    
    // Background colors
    val backgroundPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.surface
    
    val backgroundSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceVariant
    
    // Icon colors
    val iconPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.primary
    
    val iconSecondary: Color
        @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant
    
    val iconOnPrimary: Color
        @Composable get() = MaterialTheme.colorScheme.onPrimary
}
