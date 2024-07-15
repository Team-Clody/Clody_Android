package com.sopt.clody.di

import com.sopt.clody.data.remote.api.AnotherApiService
import com.sopt.clody.data.remote.api.ApiService
import com.sopt.clody.data.remote.api.DiaryListService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

// Retrofit 객체를 제공하는 모듈
@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideClodyService(@CLODY retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDiaryListService(@CLODY retrofit: Retrofit) : DiaryListService =
        retrofit.create(DiaryListService::class.java)

    @Provides
    @Singleton
    fun provideAnotherService(@ANOTHER retrofit: Retrofit): AnotherApiService =
        retrofit.create(AnotherApiService::class.java)
}
