package com.sampoom.android.core.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException

data class ApiErrorResponse(
    val code: Int? = null,
    val message: String? = null
)

fun Throwable.serverMessageOrNull(): String? {
    if (this is HttpException) {
        val errorBody = response()?.errorBody()?.string() ?: return null
        Log.d("ErrorHandling", "Error body: $errorBody")
        return try {
            Gson().fromJson(errorBody, ApiErrorResponse::class.java).message
        } catch (_: JsonSyntaxException) {
            null
        } catch (_: Exception) {
            null
        }
    }
    return null
}


