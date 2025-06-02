package com.sopt.clody.core.login

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginModule {

    @Binds
    abstract fun bindLoginSdk(
        kakaoLoginSdk: KakaoLoginSdk,
    ): LoginSdk
}
