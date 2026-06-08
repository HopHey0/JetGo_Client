package com.hophey.jetgo.core.datastorage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hophey.jetgo.feature.auth.data.api.AuthApi
import com.hophey.jetgo.feature.auth.data.dto.RefreshTokenRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore("session")

class SessionStorage(
    private val context: Context,
) {
    private val keyAccess  = stringPreferencesKey("access_token")
    private val keyRefresh = stringPreferencesKey("refresh_token")

    val isLoggedIn: Flow<Boolean> = context.sessionDataStore.data
        .map { it[keyAccess] != null }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.sessionDataStore.edit {
            it[keyAccess]  = accessToken
            it[keyRefresh] = refreshToken
        }
    }

    suspend fun getAccessToken(): String? =
        context.sessionDataStore.data.firstOrNull()?.get(keyAccess)

    suspend fun getRefreshToken(): String? =
        context.sessionDataStore.data.firstOrNull()?.get(keyRefresh)

    suspend fun clear() {
        context.sessionDataStore.edit { it.clear() }
    }
}