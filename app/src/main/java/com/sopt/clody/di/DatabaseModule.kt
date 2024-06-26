package com.sopt.clody.di

import android.content.Context
import androidx.room.Room
import com.sopt.clody.data.local.AppDatabase
import com.sopt.clody.data.local.dao.ExampleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Room Database 객체를 제공하는 모듈
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "clody_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExampleDao(database: AppDatabase): ExampleDao {
        return database.exampleDao()
    }
}
