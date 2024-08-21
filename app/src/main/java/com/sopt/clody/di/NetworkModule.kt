package com.sopt.clody.di


import android.content.Context
import android.net.ConnectivityManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sopt.clody.BuildConfig
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.remote.interceptor.AuthInterceptor
import com.sopt.clody.data.repository.ReissueTokenRepository
import com.sopt.clody.presentation.utils.network.NetworkUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton

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
    fun provideOauthInterceptor(
        reissueTokenRepository: Provider<ReissueTokenRepository>,
        tokenDataStore: TokenDataStore,
        @ApplicationContext context: Context
    ): AuthInterceptor {
        return AuthInterceptor(reissueTokenRepository, tokenDataStore, context)
    }

    @Provides
    @Singleton
    @CLODY
    fun provideClodyOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        oauthInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(oauthInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @CLODY
    fun provideClodyBaseUrl(): String = BuildConfig.CLODY_BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @CLODY
    fun provideClodyRetrofit(
        @CLODY okHttpClient: OkHttpClient,
        @CLODY baseUrl: String
    ): Retrofit = provideRetrofit(okHttpClient, baseUrl)

    @Provides
    fun provideNetworkUtil(@ApplicationContext context: Context): NetworkUtil {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return NetworkUtil(connectivityManager)
    }
}
