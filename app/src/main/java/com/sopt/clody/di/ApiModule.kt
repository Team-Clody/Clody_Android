package com.sopt.clody.di

import com.sopt.clody.data.remote.api.AccountManagementService
import com.sopt.clody.data.remote.api.AuthService
import com.sopt.clody.data.remote.api.DiaryService
import com.sopt.clody.data.remote.api.NotificationService
import com.sopt.clody.data.remote.api.TokenReissueService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideTokenReissueService(reissueRetrofit: Retrofit): TokenReissueService =
        reissueRetrofit.create(TokenReissueService::class.java)

    @Provides
    @Singleton
    fun provideDiaryService(retrofit: Retrofit): DiaryService =
        retrofit.create(DiaryService::class.java)

    @Provides
    @Singleton
    fun provideAccountManagementService(retrofit: Retrofit): AccountManagementService =
        retrofit.create(AccountManagementService::class.java)

    @Provides
    @Singleton
    fun provideNotificationService(retrofit: Retrofit): NotificationService =
        retrofit.create(NotificationService::class.java)
}

