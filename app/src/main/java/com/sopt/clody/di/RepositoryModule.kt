package com.sopt.clody.di

import com.sopt.clody.data.repository.DiaryListRepository
import com.sopt.clody.data.repositoryimpl.ExampleRepositoryImpl
import com.sopt.clody.data.repository.ExampleRepository
import com.sopt.clody.data.repositoryimpl.DiaryListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Repository 객체를 제공하는 모듈
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDiaryListRepository(
        diaryListRepositoryImpl: DiaryListRepositoryImpl
    ) : DiaryListRepository
    @Binds
    @Singleton
    abstract fun bindExampleRepository(
        exampleRepositoryImpl: ExampleRepositoryImpl
    ): ExampleRepository
}
