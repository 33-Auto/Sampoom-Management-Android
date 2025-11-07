package com.sampoom.android.core.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.feature.auth.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

// Per official guidance, DataStore instance should be single and at top-level.
private val Context.authDataStore by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val cryptoManager: CryptoManager
) {
    private val dataStore = context.authDataStore

    private object Keys {
        val ACCESS_TOKEN: Preferences.Key<String> = stringPreferencesKey("access_token")
        val REFRESH_TOKEN: Preferences.Key<String> = stringPreferencesKey("refresh_token")
        val TOKEN_EXPIRES_AT: Preferences.Key<Long> = longPreferencesKey("token_expires_at")
        val USER_ID: Preferences.Key<String> = stringPreferencesKey("user_id")
        val USER_NAME: Preferences.Key<String> = stringPreferencesKey("user_name")
        val USER_EMAIL: Preferences.Key<String> = stringPreferencesKey("user_email")
        val USER_ROLE: Preferences.Key<String> = stringPreferencesKey("user_role")
        val USER_POSITION: Preferences.Key<String> = stringPreferencesKey("user_position")
        val USER_WORKSPACE: Preferences.Key<String> = stringPreferencesKey("user_workspace")
        val USER_BRANCH: Preferences.Key<String> = stringPreferencesKey("user_branch")
        val USER_AGENCY_ID: Preferences.Key<String> = stringPreferencesKey("user_agency_id")
        val USER_STARTED_AT: Preferences.Key<String> = stringPreferencesKey("user_started_at")
        val USER_ENDED_AT: Preferences.Key<String> = stringPreferencesKey("user_ended_at")
    }

    suspend fun saveUser(user: User) {
        val expiresAt = System.currentTimeMillis() + (user.expiresIn * 1000)
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = cryptoManager.encrypt(user.accessToken)
            prefs[Keys.REFRESH_TOKEN] = cryptoManager.encrypt(user.refreshToken)
            prefs[Keys.TOKEN_EXPIRES_AT] = expiresAt
            prefs[Keys.USER_ID] = cryptoManager.encrypt(user.userId.toString())
            prefs[Keys.USER_NAME] = cryptoManager.encrypt(user.userName)
            prefs[Keys.USER_EMAIL] = cryptoManager.encrypt(user.email)
            prefs[Keys.USER_ROLE] = cryptoManager.encrypt(user.role)
            prefs[Keys.USER_POSITION] = cryptoManager.encrypt(user.position.name)
            prefs[Keys.USER_WORKSPACE] = cryptoManager.encrypt(user.workspace)
            prefs[Keys.USER_BRANCH] = cryptoManager.encrypt(user.branch)
            prefs[Keys.USER_AGENCY_ID] = cryptoManager.encrypt(user.agencyId.toString())
            prefs[Keys.USER_STARTED_AT] = cryptoManager.encrypt(user.startedAt.toString())
            prefs[Keys.USER_ENDED_AT] = cryptoManager.encrypt(user.endedAt.toString())
        }
    }

    suspend fun saveToken(accessToken: String, refreshToken: String, expiresIn: Long) {
        val expiresAt = System.currentTimeMillis() + (expiresIn * 1000)
        dataStore.edit { prefs ->
            prefs[Keys.ACCESS_TOKEN] = cryptoManager.encrypt(accessToken)
            prefs[Keys.REFRESH_TOKEN] = cryptoManager.encrypt(refreshToken)
            prefs[Keys.TOKEN_EXPIRES_AT] = expiresAt
        }
    }

    suspend fun getStoredUser(): User? {
        val prefs = dataStore.data.first()
        val userId = prefs[Keys.USER_ID]
        val userName = prefs[Keys.USER_NAME]
        val userEmail = prefs[Keys.USER_EMAIL]
        val userRole = prefs[Keys.USER_ROLE]
        val accessToken = prefs[Keys.ACCESS_TOKEN]
        val refreshToken = prefs[Keys.REFRESH_TOKEN]
        val expiresAt = prefs[Keys.TOKEN_EXPIRES_AT]
        val userPosition = prefs[Keys.USER_POSITION]
        val userWorkspace = prefs[Keys.USER_WORKSPACE]
        val userBranch = prefs[Keys.USER_BRANCH]
        val userAgencyId = prefs[Keys.USER_AGENCY_ID]
        val userStartedAt = prefs[Keys.USER_STARTED_AT]
        val userEndedAt = prefs[Keys.USER_ENDED_AT]

        if (userId != null && userName != null && userEmail != null && userRole != null &&
            accessToken != null && refreshToken != null && userPosition != null && userWorkspace != null && userBranch != null && userAgencyId != null && userStartedAt != null && userEndedAt != null
        ) {
            try {
                val remaining = expiresAt?.let {
                    kotlin.math.max(0L, (it - System.currentTimeMillis()) / 1000)
                } ?: 0L

                return User(
                    cryptoManager.decrypt(userId).toLong(),
                    cryptoManager.decrypt(userName),
                    cryptoManager.decrypt(userEmail),
                    cryptoManager.decrypt(userRole),
                    cryptoManager.decrypt(accessToken),
                    cryptoManager.decrypt(refreshToken),
                    remaining,
                    cryptoManager.decrypt(userPosition).let { decrypted ->
                        try { UserPosition.valueOf(decrypted.uppercase()) } catch (_: Exception) { UserPosition.STAFF }
                    },
                    cryptoManager.decrypt(userWorkspace),
                    cryptoManager.decrypt(userBranch),
                    cryptoManager.decrypt(userAgencyId).toLong(),
                    cryptoManager.decrypt(userStartedAt),
                    cryptoManager.decrypt(userEndedAt),
                )
            } catch (e: Exception) {
                return null
            }
        } else return null
    }

    suspend fun getAccessToken(): String? {
        val encrypted = dataStore.data.first()[Keys.ACCESS_TOKEN] ?: return null
        return try {
            cryptoManager.decrypt(encrypted)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRefreshToken(): String? {
        val encrypted = dataStore.data.first()[Keys.REFRESH_TOKEN] ?: return null
        return try {
            cryptoManager.decrypt(encrypted)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun isTokenExpired(): Boolean {
        val expiresAt = dataStore.data.first()[Keys.TOKEN_EXPIRES_AT]
        return expiresAt == null || System.currentTimeMillis() > expiresAt
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    suspend fun hasToken(): Boolean {
        val accessToken = getAccessToken()
        val refreshToken = getRefreshToken()
        return !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
    }
}