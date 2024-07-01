package com.sopt.clody.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sopt.clody.BuildConfig.ANOTHER_BASE_URL
import com.sopt.clody.BuildConfig.CLODY_BASE_URL
import com.sopt.clody.data.remote.interceptor.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

// OkHttpClient와 Retrofit 인스턴스 제공
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    @CLODY
    fun provideTokenInterceptor(tokenInterceptor: TokenInterceptor): Interceptor = tokenInterceptor

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenInterceptor: Interceptor? = null
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        tokenInterceptor?.let { builder.addInterceptor(it) }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @CLODY baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    @CLODY
    fun provideClodyRetrofit(
        @CLODY okHttpClient: OkHttpClient
    ): Retrofit = provideRetrofit(okHttpClient, CLODY_BASE_URL)

    @Provides
    @Singleton
    @ANOTHER
    fun provideAnotherRetrofit(
        @ANOTHER okHttpClient: OkHttpClient
    ): Retrofit = provideRetrofit(okHttpClient, ANOTHER_BASE_URL)

    @Provides
    @Singleton
    @CLODY
    fun provideClodyOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @CLODY tokenInterceptor: Interceptor
    ): OkHttpClient = provideOkHttpClient(loggingInterceptor, tokenInterceptor)

    @Provides
    @Singleton
    @ANOTHER
    fun provideAnotherOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = provideOkHttpClient(loggingInterceptor)
}
