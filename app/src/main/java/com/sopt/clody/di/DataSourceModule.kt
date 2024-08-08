package com.sopt.clody.di

import com.sopt.clody.data.local.datasource.LocalDataSource
import com.sopt.clody.data.local.datasourceimpl.LocalDataSourceImpl
import com.sopt.clody.data.remote.datasource.AccountManagementDataSource
import com.sopt.clody.data.remote.datasource.AuthDataSource
import com.sopt.clody.data.remote.datasource.DailyDiariesDataSource
import com.sopt.clody.data.remote.datasource.DailyDiaryListDataSource
import com.sopt.clody.data.remote.datasource.DiaryListDataSource
import com.sopt.clody.data.remote.datasource.DiaryTimeDataSource
import com.sopt.clody.data.remote.datasource.MonthlyCalendarDataSource
import com.sopt.clody.data.remote.datasource.NotificationDataSource
import com.sopt.clody.data.remote.datasource.ReissueTokenDataSource
import com.sopt.clody.data.remote.datasource.ReplyDiaryDataSource
import com.sopt.clody.data.remote.datasource.WriteDiaryDataSource
import com.sopt.clody.data.remote.datasourceimpl.AccountManagementDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.AuthDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.DailyDiariesDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.DailyDiaryListDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.DiaryListDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.DiaryTimeDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.MonthlyCalendarDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.NotificationDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.ReissueTokenDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.ReplyDiaryDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.WriteDiaryDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// 로컬 및 원격 데이터 소스 제공

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        authDataSourceImpl: AuthDataSourceImpl
    ): AuthDataSource


    @Binds
    @Singleton
    abstract fun bindReissueTokenDataSource(
        reissueTokenDataSourceImpl: ReissueTokenDataSourceImpl
    ): ReissueTokenDataSource

    @Binds
    @Singleton
    abstract fun bindMonthlyCalendarRemoteDataSource(
        monthlyCalendarDataSourceImpl: MonthlyCalendarDataSourceImpl
    ): MonthlyCalendarDataSource

    @Binds
    @Singleton
    abstract fun bindDailyDiaryListRemoteDataSource(
        dailyDiaryListDataSourceImpl: DailyDiaryListDataSourceImpl
    ): DailyDiaryListDataSource

    @Binds
    @Singleton
    abstract fun bindDailyDiariesRemoteDataSource(
        dailyDiariesDataSourceImpl: DailyDiariesDataSourceImpl
    ): DailyDiariesDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindDiaryListDataSource(
        diaryListDataSourceImpl: DiaryListDataSourceImpl
    ): DiaryListDataSource

    @Binds
    @Singleton
    abstract fun bindAccountManagementDataSource(
        accountManagementDataSourceImpl: AccountManagementDataSourceImpl
    ): AccountManagementDataSource

    @Binds
    @Singleton
    abstract fun bindWriteDiaryDataSource(
        writeDiaryDataSourceImpl: WriteDiaryDataSourceImpl
    ): WriteDiaryDataSource

    @Binds
    @Singleton
    abstract fun bindReplyDiaryDataSource(
        replyDiaryDataSourceImpl: ReplyDiaryDataSourceImpl
    ): ReplyDiaryDataSource

    @Binds
    @Singleton
    abstract fun bindDiaryTimeDataSource(
        diaryTimeDataSourceImpl: DiaryTimeDataSourceImpl
    ): DiaryTimeDataSource

    @Binds
    @Singleton
    abstract fun bindNotificationDataSource(
        notificationDataSourceImpl: NotificationDataSourceImpl
    ): NotificationDataSource
}
