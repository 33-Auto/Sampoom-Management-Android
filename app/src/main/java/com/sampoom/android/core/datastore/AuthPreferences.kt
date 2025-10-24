package com.sampoom.android.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sampoom.android.feature.user.domain.model.User
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
        val TOKEN_EXPIRES_AT: Preferences.Key<Long> = longPreferencesKey("token_expires_at")
        val USER_ID: Preferences.Key<Long> = longPreferencesKey("user_id")
        val USER_NAME: Preferences.Key<String> = stringPreferencesKey("user_name")
        val USER_ROLE: Preferences.Key<String> = stringPreferencesKey("user_role")
    }

    suspend fun saveUser(user: User) {
        val expiresAt = System.currentTimeMillis() + (user.expiresIn * 1000)
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = user.accessToken
            prefs[Keys.REFRESH_TOKEN] = user.refreshToken
            prefs[Keys.TOKEN_EXPIRES_AT] = expiresAt
            prefs[Keys.USER_ID] = user.userId
            prefs[Keys.USER_NAME] = user.userName
            prefs[Keys.USER_ROLE] = user.role
        }
    }

    // Suspend save to avoid blocking thread
    suspend fun saveToken(accessToken: String, refreshToken: String, expiresIn: Long) {
        val expiresAt = System.currentTimeMillis() + (expiresIn * 1000)
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = accessToken
            prefs[Keys.REFRESH_TOKEN] = refreshToken
            prefs[Keys.TOKEN_EXPIRES_AT] = expiresAt
        }
    }

    fun getStoredUser(): User? = runBlocking {
        val userId = dataStore.data.first()[Keys.USER_ID]
        val userName = dataStore.data.first()[Keys.USER_NAME]
        val userRole = dataStore.data.first()[Keys.USER_ROLE]
        val accessToken = dataStore.data.first()[Keys.ACCESS_TOKEN]
        val refreshToken = dataStore.data.first()[Keys.REFRESH_TOKEN]

        if (userId != null && userName != null && userRole != null &&
            accessToken != null && refreshToken != null) {
            User(userId, userName, userRole, accessToken, refreshToken, 0)
        } else null
    }

    // Synchronous getters backed by runBlocking for minimal surface change
    fun getAccessToken(): String? = runBlocking {
        dataStore.data.first()[Keys.ACCESS_TOKEN]
    }

    fun getRefreshToken(): String? = runBlocking {
        dataStore.data.first()[Keys.REFRESH_TOKEN]
    }

    fun isTokenExpired(): Boolean {
        val expiresAt = runBlocking { dataStore.data.first()[Keys.TOKEN_EXPIRES_AT] }
        return expiresAt == null || System.currentTimeMillis() > expiresAt
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    fun hasToken(): Boolean = !getAccessToken().isNullOrEmpty() && !getRefreshToken().isNullOrEmpty()
}