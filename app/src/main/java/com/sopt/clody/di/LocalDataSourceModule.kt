package com.sopt.clody.di

import android.content.SharedPreferences
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.datastore.TokenDataStoreImpl
import com.sopt.clody.data.local.datasource.FirstDraftLocalDataSource
import com.sopt.clody.data.local.datasourceimpl.FirstDraftLocalDataSourceImpl
import com.sopt.clody.di.qualifier.FirstDraftPrefs
import com.sopt.clody.di.qualifier.TokenPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    @Provides
    @Singleton
    fun provideTokenDataStore(@TokenPrefs sharedPreferences: SharedPreferences): TokenDataStore =
        TokenDataStoreImpl(sharedPreferences)

    @Provides
    @Singleton
    fun provideFirstDraftLocalDataSource(@FirstDraftPrefs sharedPreferences: SharedPreferences): FirstDraftLocalDataSource =
        FirstDraftLocalDataSourceImpl(sharedPreferences)
}
