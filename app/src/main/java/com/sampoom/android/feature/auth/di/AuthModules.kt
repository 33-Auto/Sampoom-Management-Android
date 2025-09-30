package com.sampoom.android.feature.auth.di

import com.sampoom.android.feature.auth.data.remote.api.AuthApi
import com.sampoom.android.feature.auth.data.repository.AuthRepositoryImpl
import com.sampoom.android.feature.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBinModule {
    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
object AuthProvideModule {
    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
}