package com.sampoom.android.feature.auth.data.local.preferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
){
    private val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token)
    }
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
    fun hasToken(): Boolean = !sharedPreferences.getString("token", null).isNullOrEmpty()
}