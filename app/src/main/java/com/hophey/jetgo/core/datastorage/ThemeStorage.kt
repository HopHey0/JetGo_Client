package com.hophey.jetgo.core.datastorage

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore by preferencesDataStore("theme_prefs")

class ThemeStorage(private val context: Context) {

    private val keyDarkTheme = booleanPreferencesKey("is_dark_theme")

    val isDarkTheme: Flow<Boolean> = context.themeDataStore.data
        .map { it[keyDarkTheme] ?: false }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.themeDataStore.edit { it[keyDarkTheme] = enabled }
    }
}