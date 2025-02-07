package com.sopt.clody.di

import com.sopt.clody.data.remote.api.AccountManagementService
import com.sopt.clody.data.remote.api.AuthService
import com.sopt.clody.data.remote.api.NotificationService
import com.sopt.clody.data.remote.api.ReIssueService
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
    fun provideMonthlyCalendarService(retrofit: Retrofit): CalendarApiService =
        retrofit.create(CalendarApiService::class.java)

    @Provides
    @Singleton
    fun provideDiaryListService(retrofit: Retrofit): DiaryListService =
        retrofit.create(DiaryListService::class.java)

    @Provides
    @Singleton
    fun provideDailyDiaryListService(retrofit: Retrofit): DailyDiaryListService =
        retrofit.create(DailyDiaryListService::class.java)

    @Provides
    @Singleton
    fun provideAccountManagementService(retrofit: Retrofit): AccountManagementService =
        retrofit.create(AccountManagementService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideReissueTokenService(reissueRetrofit: Retrofit): ReIssueService =
        reissueRetrofit.create(ReIssueService::class.java)

    @Provides
    @Singleton
    fun provideWriteDiaryService(retrofit: Retrofit): WriteDiaryService =
        retrofit.create(WriteDiaryService::class.java)

    @Provides
    @Singleton
    fun provideReplyDiaryService(retrofit: Retrofit): ReplyDiaryService =
        retrofit.create(ReplyDiaryService::class.java)

    @Provides
    @Singleton
    fun provideDiaryTimeService(retrofit: Retrofit): DiaryTimeService =
        retrofit.create(DiaryTimeService::class.java)

    @Provides
    @Singleton
    fun provideNotificationService(retrofit: Retrofit): NotificationService =
        retrofit.create(NotificationService::class.java)
}

