package com.sopt.clody.presentation.utils.network

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ErrorMessageModule {

    @Provides
    @Singleton
    fun provideErrorMessageProvider(
        @ApplicationContext context: Context,
    ): ErrorMessageProvider {
        return ErrorMessageProviderImpl(context)
    }
}
