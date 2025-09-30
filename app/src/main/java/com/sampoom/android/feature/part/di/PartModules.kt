package com.sampoom.android.feature.part.di

import com.sampoom.android.feature.part.data.remote.api.PartApi
import com.sampoom.android.feature.part.data.repository.PartRepositoryImpl
import com.sampoom.android.feature.part.domain.repository.PartRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PartBinModule {
    @Binds @Singleton
    abstract fun bindPartRepository(impl: PartRepositoryImpl): PartRepository
}

@Module
@InstallIn(SingletonComponent::class)
object PartModule {
    @Provides @Singleton
    fun providePartApi(retrofit: Retrofit): PartApi = retrofit.create(PartApi::class.java)
}