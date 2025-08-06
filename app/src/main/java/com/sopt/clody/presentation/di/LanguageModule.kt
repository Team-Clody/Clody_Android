package com.sopt.clody.presentation.di

import com.sopt.clody.presentation.utils.language.LanguageProvider
import com.sopt.clody.presentation.utils.language.LanguageProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LanguageModule {

    @Binds
    fun bindLanguageProvider(
        languageProviderImpl: LanguageProviderImpl,
    ): LanguageProvider
}
