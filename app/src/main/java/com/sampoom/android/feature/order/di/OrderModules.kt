package com.sampoom.android.feature.order.di

import com.sampoom.android.feature.order.data.remote.api.OrderApi
import com.sampoom.android.feature.order.data.repository.OrderRepositoryImpl
import com.sampoom.android.feature.order.domain.repository.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OrderBindModule {
    @Binds @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository
}

@Module
@InstallIn(SingletonComponent::class)
object OrderModule {
    @Provides @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApi = retrofit.create(OrderApi::class.java)
}