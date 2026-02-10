package com.example.quicknotes.presentation.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

private val iconMap: Map<String, ImageVector> = mapOf(
    "folder.fill" to Icons.Filled.Folder,
    "briefcase.fill" to Icons.Filled.Work,
    "person.fill" to Icons.Filled.Person,
    "lightbulb.fill" to Icons.Filled.Lightbulb,
    "heart.fill" to Icons.Filled.Favorite,
    "star.fill" to Icons.Filled.Star,
    "tag.fill" to Icons.Filled.Label,
    "book.fill" to Icons.Filled.Book,
    "house.fill" to Icons.Filled.Home,
    "envelope.fill" to Icons.Filled.Email,
    "camera.fill" to Icons.Filled.CameraAlt,
    "music.note" to Icons.Filled.MusicNote,
    "sportscourt.fill" to Icons.Filled.Sports,
    "cart.fill" to Icons.Filled.ShoppingCart,
    "gift.fill" to Icons.Filled.Star, // Gift-like; CardGift not in all icon pack versions
)

fun iconIdToImageVector(iconId: String): ImageVector =
    iconMap[iconId] ?: Icons.Filled.Folder
