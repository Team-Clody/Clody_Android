package com.sopt.clody.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sopt.clody.data.remote.api.AccountManagementService
import com.sopt.clody.data.remote.api.AnotherApiService
import com.sopt.clody.data.remote.api.ApiService
import com.sopt.clody.data.remote.api.AuthService
import com.sopt.clody.data.remote.api.CalendarApiService
import com.sopt.clody.data.remote.api.DiaryListService
import com.sopt.clody.data.remote.api.ReIssueService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
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
    fun provideMonthlyCalendarService(@CLODY retrofit: Retrofit): CalendarApiService =
        retrofit.create(CalendarApiService::class.java)

    @Provides
    @Singleton
    fun provideDiaryListService(@CLODY retrofit: Retrofit): DiaryListService =
        retrofit.create(DiaryListService::class.java)

    @Provides
    @Singleton
    fun provideAccountManagementService(@CLODY retrofit: Retrofit): AccountManagementService =
        retrofit.create(AccountManagementService::class.java)

    @Provides
    @Singleton
    fun provideAnotherService(@ANOTHER retrofit: Retrofit): AnotherApiService =
        retrofit.create(AnotherApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(@CLODY retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideReissueRetrofit(@CLODY okHttpClient: OkHttpClient, @CLODY baseUrl: String): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideReissueTokenService(@CLODY reissueRetrofit: Retrofit): ReIssueService =
        reissueRetrofit.create(ReIssueService::class.java)
}

