package com.sampoom.android.core.di

import com.sampoom.android.core.util.GlobalMessageHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiModule {
    @Provides
    @Singleton
    fun provideGlobalErrorHandler(): GlobalMessageHandler = GlobalMessageHandler()
}