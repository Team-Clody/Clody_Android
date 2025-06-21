package com.sopt.clody.core.security.login

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {

    @Binds
    abstract fun bindLoginSecurityChecker(
        impl: DefaultLoginSecurityChecker,
    ): LoginSecurityChecker
}
