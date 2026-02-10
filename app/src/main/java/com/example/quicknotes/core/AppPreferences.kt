package com.example.quicknotes.core

import android.content.Context
import android.content.SharedPreferences

/**
 * App-level preferences for onboarding, appearance, and settings.
 */
class AppPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var hasCompletedOnboarding: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING, value).apply()

    var isDarkModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_DARK_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_DARK_MODE, value).apply()

    var isNotificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS, value).apply()

    var sortOrder: String
        get() = prefs.getString(KEY_SORT_ORDER, SortOrder.DATE_DESCENDING) ?: SortOrder.DATE_DESCENDING
        set(value) = prefs.edit().putString(KEY_SORT_ORDER, value).apply()

    object SortOrder {
        const val DATE_DESCENDING = "Newest first"
        const val DATE_ASCENDING = "Oldest first"
        const val TITLE_ASCENDING = "Title A–Z"
        const val TITLE_DESCENDING = "Title Z–A"
        val all: List<String> = listOf(DATE_DESCENDING, DATE_ASCENDING, TITLE_ASCENDING, TITLE_DESCENDING)
    }

    companion object {
        private const val PREFS_NAME = "quicknotes_prefs"
        private const val KEY_ONBOARDING = "has_completed_onboarding"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_SORT_ORDER = "sort_order"
    }
}
