package com.sopt.clody.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.sopt.clody.data.datastore.SecureDataStore
import com.sopt.clody.data.datastore.SecureDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "clody_data_store")

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Singleton
    @Provides
    fun provideSecureDataStore(secureDataSourceImpl: SecureDataStoreImpl): SecureDataStore {
        return secureDataSourceImpl
    }
}
