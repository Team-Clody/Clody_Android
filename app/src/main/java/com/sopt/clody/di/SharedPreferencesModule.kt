package com.sopt.clody.di

import android.content.Context
import android.content.SharedPreferences
import com.sopt.clody.data.local.datasource.AppReviewLocalDataSource
import com.sopt.clody.data.local.datasourceimpl.AppReviewLocalDataSourceImpl
import com.sopt.clody.di.qualifier.ReviewPrefs
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
    @Provides
    @Singleton
    @TokenPrefs
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @ReviewPrefs
    fun provideReviewSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("review_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppReviewLocalDataSource(@ReviewPrefs sharedPreferences: SharedPreferences): AppReviewLocalDataSource =
        AppReviewLocalDataSourceImpl(sharedPreferences)
}
