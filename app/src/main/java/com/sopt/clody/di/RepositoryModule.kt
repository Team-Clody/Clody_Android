package com.sopt.clody.di

import com.sopt.clody.domain.repository.AccountManagementRepository
import com.sopt.clody.domain.repository.AuthRepository
import com.sopt.clody.domain.repository.DiaryRepository
import com.sopt.clody.domain.repository.NotificationRepository
import com.sopt.clody.data.repository.ReissueTokenRepository
import com.sopt.clody.domain.repository.TokenRepository
import com.sopt.clody.data.repositoryimpl.AccountManagementRepositoryImpl
import com.sopt.clody.data.repositoryimpl.AuthRepositoryImpl
import com.sopt.clody.data.repositoryimpl.DiaryRepositoryImpl
import com.sopt.clody.data.repositoryimpl.NotificationRepositoryImpl
import com.sopt.clody.data.repositoryimpl.ReissueTokenRepositoryImpl
import com.sopt.clody.data.repositoryimpl.TokenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    @Singleton
    abstract fun bindReissueTokenRepository(
        reissueTokenRepositoryImpl: ReissueTokenRepositoryImpl
    ): ReissueTokenRepository

    @Binds
    @Singleton
    abstract fun bindDiaryRepository(
        diaryRepositoryImpl: DiaryRepositoryImpl
    ): DiaryRepository

    @Binds
    @Singleton
    abstract fun bindAccountManagementRepository(
        accountManagementRepositoryImpl: AccountManagementRepositoryImpl
    ): AccountManagementRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository
}
