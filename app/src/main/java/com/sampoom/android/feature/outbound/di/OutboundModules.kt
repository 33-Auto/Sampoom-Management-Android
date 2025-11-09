package com.sampoom.android.feature.outbound.di

import com.sampoom.android.feature.outbound.data.remote.api.OutboundApi
import com.sampoom.android.feature.outbound.data.repository.OutboundRepositoryImpl
import com.sampoom.android.feature.outbound.domain.repository.OutboundRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OutboundBinModule {
    @Binds @Singleton
    abstract fun bindOutboundRepository(impl: OutboundRepositoryImpl): OutboundRepository
}

@Module
@InstallIn(SingletonComponent::class)
object OutboundModule {
    @Provides @Singleton
    fun provideOutboundApi(retrofit: Retrofit): OutboundApi = retrofit.create(OutboundApi::class.java)
}