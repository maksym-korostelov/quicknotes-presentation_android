package com.example.quicknotes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography design system for QuickNotes Android app.
 * Provides consistent text styles across the application.
 *
 * Two ways to apply typography (aligned with iOS):
 * - Font + default color: use semantic composables when available (e.g. bodyMediumValue(), bodyLargeAction())
 *   or pass style + color explicitly (e.g. style = AppTypography.bodyLarge, color = AppColors.textSecondary).
 * - Font only: use style = AppTypography.xxx (e.g. style = AppTypography.headingSmall).
 *
 * Based on Material Design 3 type scale with custom semantic tokens.
 */
object AppTypography {
    
    // Display sizes - for large prominent text
    val displayLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 34.sp,
            lineHeight = 41.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        )
    
    val displayMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 28.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp
        )
    
    // Headings - for section titles
    val headingLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        )
    
    val headingMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 20.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.15.sp
        )
    
    val headingSmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 17.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.sp
        )
    
    // Body text - for main content
    val bodyLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 17.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        )
    
    val bodyMedium: TextStyle
        @Composable get() = TextStyle(
            fontSize = 15.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        )
    
    val bodySmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        )
    
    // Captions - for supporting text
    val captionLarge: TextStyle
        @Composable get() = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        )
    
    val captionSmall: TextStyle
        @Composable get() = TextStyle(
            fontSize = 11.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.sp
        )
    
    // Semantic tokens - with predefined colors
    @Composable
    fun bodyLargeDestructive(): Pair<TextStyle, Color> = 
        bodyLarge to MaterialTheme.colorScheme.error
    
    @Composable
    fun bodyLargeAction(): Pair<TextStyle, Color> = 
        bodyLarge to MaterialTheme.colorScheme.primary
    
    @Composable
    fun bodyMediumValue(): Pair<TextStyle, Color> = 
        bodyMedium to MaterialTheme.colorScheme.onSurfaceVariant
    
    @Composable
    fun bodySmallHint(): Pair<TextStyle, Color> = 
        bodySmall to MaterialTheme.colorScheme.onSurfaceVariant
    
    // Icon hero sizes - for large decorative icons
    val iconHeroLarge: Int = 64  // 64dp
    val iconHeroXLarge: Int = 72  // 72dp
    val iconHeroXXLarge: Int = 80  // 80dp
    val iconHeroMedium: Int = 48  // 48dp
    
    // Label tokens for status badges (using captionSmall as base)
    @Composable
    fun labelArchived(): Pair<TextStyle, Color> = 
        captionSmall to MaterialTheme.colorScheme.onSurfaceVariant
    
    @Composable
    fun labelCompleted(): Pair<TextStyle, Color> = 
        captionSmall to MaterialTheme.colorScheme.primary
    
    @Composable
    fun labelArchivedCompleted(): Pair<TextStyle, Color> = 
        captionSmall to MaterialTheme.colorScheme.tertiary
}

/**
 * Extension function to apply AppTypography tokens to Text composables.
 * 
 * Usage:
 * ```
 * Text(
 *     "Hello World",
 *     modifier = Modifier.appTypography(AppTypography.bodyLarge)
 * )
 * 
 * // With color override
 * Text(
 *     "Hello World",
 *     modifier = Modifier.appTypography(AppTypography.bodyLarge, AppColors.textSecondary)
 * )
 * ```
 */
@Composable
fun appTypographyStyle(
    style: TextStyle,
    colorOverride: Color? = null
): Pair<TextStyle, Color?> {
    return style to colorOverride
}
