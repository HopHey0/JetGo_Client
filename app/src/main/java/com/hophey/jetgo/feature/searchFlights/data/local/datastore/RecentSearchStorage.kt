package com.hophey.jetgo.feature.searchFlights.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hophey.jetgo.feature.searchFlights.data.dto.RecentSearchDto
import com.hophey.jetgo.feature.searchFlights.domain.model.RecentSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "recent_searches")

class RecentSearchStorage(private val context: Context) {

    private val key = stringPreferencesKey("recent_searches_list")

    val searches: Flow<List<RecentSearchDto>> = context.dataStore.data.map { prefs ->
        val raw = prefs[key] ?: return@map emptyList()
        runCatching {
            Json.decodeFromString<List<RecentSearchDto>>(raw)
        }.getOrElse { emptyList() }
    }

    suspend fun save(searches: List<RecentSearchDto>) {
        context.dataStore.edit { prefs ->
            prefs[key] = Json.encodeToString(searches)
        }
    }

    suspend fun clear(){
        context.dataStore .edit { prefs ->
            prefs.remove(key)
        }
    }
}