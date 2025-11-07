package com.sampoom.android.feature.dashboard.di

import com.sampoom.android.feature.dashboard.data.remote.api.DashboardApi
import com.sampoom.android.feature.dashboard.data.repository.DashboardRepositoryImpl
import com.sampoom.android.feature.dashboard.domain.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardBinModule {
    @Binds @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {
    @Provides @Singleton
    fun provideDashboardApi(retrofit: Retrofit): DashboardApi = retrofit.create(DashboardApi::class.java)
}