package com.sampoom.android.feature.auth.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// Per official guidance, DataStore instance should be single and at top-level.
private val Context.authDataStore by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
){
    private val dataStore = context.authDataStore

    private object Keys {
        val ACCESS_TOKEN: Preferences.Key<String> = stringPreferencesKey("access_token")
        val REFRESH_TOKEN: Preferences.Key<String> = stringPreferencesKey("refresh_token")
    }

    // Suspend save to avoid blocking thread
    suspend fun saveToken(accessToken: String, refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = accessToken
            prefs[Keys.REFRESH_TOKEN] = refreshToken
        }
    }

    // Synchronous getters backed by runBlocking for minimal surface change
    fun getAccessToken(): String? = runBlocking {
        dataStore.data.first()[Keys.ACCESS_TOKEN]
    }

    fun getRefreshToken(): String? = runBlocking {
        dataStore.data.first()[Keys.REFRESH_TOKEN]
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    fun hasToken(): Boolean = !getAccessToken().isNullOrEmpty() && !getRefreshToken().isNullOrEmpty()
}