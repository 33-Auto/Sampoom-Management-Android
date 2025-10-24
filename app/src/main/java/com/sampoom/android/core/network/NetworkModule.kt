package com.sampoom.android.core.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.google.gson.GsonBuilder
import com.google.gson.FieldNamingPolicy
import com.sampoom.android.BuildConfig
import com.sampoom.android.core.datastore.AuthPreferences
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideTokenInterceptor(
        authPreferences: AuthPreferences
    ): TokenInterceptor {
        return TokenInterceptor(authPreferences)
    }

    @Provides
    @Singleton
    fun provideTokenRefreshService(
        authPreferences: AuthPreferences
    ): TokenRefreshService {
        return TokenRefreshService(authPreferences)
    }

    @Provides
    @Singleton
    fun provideTokenRefreshInterceptor(
        authPreferences: AuthPreferences,
        tokenRefreshService: TokenRefreshService
    ): TokenRefreshInterceptor {
        return TokenRefreshInterceptor(authPreferences, tokenRefreshService)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenInterceptor: TokenInterceptor,
        tokenRefreshInterceptor: TokenRefreshInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            })
            .addInterceptor(tokenInterceptor) // 토큰 자동 삽입
            .addInterceptor(tokenRefreshInterceptor) // 토큰 갱신
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create()
        return Retrofit.Builder()
            .baseUrl("https://sampoom.store/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}