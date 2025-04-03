package com.sopt.clody.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.sopt.clody.data.remote.appupdate.AppUpdateCheckerImpl
import com.sopt.clody.data.remote.datasource.RemoteConfigDataSource
import com.sopt.clody.domain.appupdate.AppUpdateChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppUpdateModule {

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig =
        FirebaseRemoteConfig.getInstance()

    @Provides
    @Singleton
    fun provideAppUpdateChecker(
        remoteConfigDataSource: RemoteConfigDataSource,
    ): AppUpdateChecker = AppUpdateCheckerImpl(remoteConfigDataSource)
}
