package com.sopt.clody.data.datastore

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideOAuthDataStore(
        @ApplicationContext context: Context,
    ): OAuthDataStore {
        return OAuthDataStore(context)
    }
}
