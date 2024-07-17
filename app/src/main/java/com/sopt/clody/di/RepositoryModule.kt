package com.sopt.clody.di

import com.sopt.clody.data.repository.AuthRepository
import com.sopt.clody.data.repository.DailyDiariesRepository
import com.sopt.clody.data.repository.DiaryListRepository
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import com.sopt.clody.data.repository.ReissueTokenRepository
import com.sopt.clody.data.repository.TokenRepository
import com.sopt.clody.data.repositoryimpl.AuthRepositoryImpl
import com.sopt.clody.data.repositoryimpl.DailyDiariesRepositoryImpl
import com.sopt.clody.data.repositoryimpl.DiaryListRepositoryImpl
import com.sopt.clody.data.repositoryimpl.MonthlyCalendarRepositoryImpl
import com.sopt.clody.data.repositoryimpl.ReissueTokenRepositoryImpl
import com.sopt.clody.data.repositoryimpl.TokenRepositoryImpl
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
    abstract fun bindDiaryListRepository(
        diaryListRepositoryImpl: DiaryListRepositoryImpl
    ): DiaryListRepository

    @Binds
    @Singleton
    abstract fun bindMonthlyCalendarRepository(
        monthlyCalendarRepositoryImpl: MonthlyCalendarRepositoryImpl
    ): MonthlyCalendarRepository

    @Binds
    @Singleton
    abstract fun bindDailyDiariesRepository(
        dailyDiariesRepositoryImpl: DailyDiariesRepositoryImpl
    ): DailyDiariesRepository

}
