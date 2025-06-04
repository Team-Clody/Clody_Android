package com.sopt.clody.di

import android.content.Context
import android.content.SharedPreferences
import com.sopt.clody.data.local.datasource.FirstDraftLocalDataSource
import com.sopt.clody.data.local.datasourceimpl.FirstDraftLocalDataSourceImpl
import com.sopt.clody.di.qualifier.FirstDraftPrefs
import com.sopt.clody.di.qualifier.TokenPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @TokenPrefs
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)
    }

    @FirstDraftPrefs
    @Provides
    @Singleton
    fun provideFirstDraftSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("first_draft_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFirstDraftLocalDataSource(@FirstDraftPrefs sharedPreferences: SharedPreferences): FirstDraftLocalDataSource =
        FirstDraftLocalDataSourceImpl(sharedPreferences)
}
