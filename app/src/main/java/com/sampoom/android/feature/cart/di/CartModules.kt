package com.sampoom.android.feature.cart.di

import com.sampoom.android.feature.cart.data.remote.api.CartApi
import com.sampoom.android.feature.cart.data.repository.CartRepositoryImpl
import com.sampoom.android.feature.cart.domain.repository.CartRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
abstract class CartBinModule {
    @Binds @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
}

@Module
@InstallIn(SingletonComponent::class)
object CartModule {
    @Provides @Singleton
    fun provideCartApi(retrofit: Retrofit): CartApi = retrofit.create(CartApi::class.java)
}