package com.sopt.clody.di

import com.sopt.clody.data.local.datasource.LocalDataSource
import com.sopt.clody.data.local.datasourceimpl.LocalDataSourceImpl
import com.sopt.clody.data.remote.datasource.DiaryListDataSource
import com.sopt.clody.data.remote.datasource.RemoteDataSource
import com.sopt.clody.data.remote.datasource.DailyDiariesDataSource
import com.sopt.clody.data.remote.datasource.MonthlyCalendarDataSource
import com.sopt.clody.data.remote.datasourceimpl.DiaryListDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.DailyDiariesDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.MonthlyCalendarDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.RemoteDataSourceImpl
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
    abstract fun bindRemoteDataSource(
        remoteDataSourceImpl: RemoteDataSourceImpl
    ): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMonthlyCalendarRemoteDataSource(
        monthlyCalendarDataSourceImpl: MonthlyCalendarDataSourceImpl
    ): MonthlyCalendarDataSource

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
    ) : DiaryListDataSource
}

