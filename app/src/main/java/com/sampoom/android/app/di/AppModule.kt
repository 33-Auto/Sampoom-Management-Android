package com.sampoom.android.app.di

import com.sampoom.android.core.common.DispatcherProvider
import com.sampoom.android.core.common.DispatcherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds @Singleton
    abstract fun bindDispatchers(impl: DispatcherProviderImpl): DispatcherProvider
}