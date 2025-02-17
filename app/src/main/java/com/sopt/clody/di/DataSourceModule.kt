package com.sopt.clody.di

import com.sopt.clody.data.remote.datasource.AccountManagementDataSource
import com.sopt.clody.data.remote.datasource.AuthDataSource
import com.sopt.clody.data.remote.datasource.DiaryRemoteDataSource
import com.sopt.clody.data.remote.datasource.NotificationDataSource
import com.sopt.clody.data.remote.datasource.TokenReissueDataSource
import com.sopt.clody.data.remote.datasourceimpl.AccountManagementDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.AuthDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.DiaryRemoteDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.NotificationDataSourceImpl
import com.sopt.clody.data.remote.datasourceimpl.TokenReissueDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    abstract fun bindTokenReissueDataSource(
        tokenReissueDataSourceImpl: TokenReissueDataSourceImpl
    ): TokenReissueDataSource

    @Binds
    @Singleton
    abstract fun bindDiaryRemoteDataSource(
        diaryRemoteDataSourceImpl: DiaryRemoteDataSourceImpl
    ): DiaryRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAccountManagementDataSource(
        accountManagementDataSourceImpl: AccountManagementDataSourceImpl
    ): AccountManagementDataSource

    @Binds
    @Singleton
    abstract fun bindNotificationDataSource(
        notificationDataSourceImpl: NotificationDataSourceImpl
    ): NotificationDataSource
}
