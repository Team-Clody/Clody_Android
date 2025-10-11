package com.sopt.clody.presentation.di

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.sopt.clody.presentation.ui.auth.signup.SignUpViewModel
import com.sopt.clody.presentation.ui.home.screen.HomeViewModel
import com.sopt.clody.presentation.ui.login.LoginViewModel
import com.sopt.clody.presentation.ui.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun bindSplashViewModelFactory(
        factory: SplashViewModel.Factory,
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginViewModelFactory(
        factory: LoginViewModel.Factory,
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    fun bindSignUpViewModelFactory(
        factory: SignUpViewModel.Factory,
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModelFactory(
        factory: HomeViewModel.Factory,
    ): AssistedViewModelFactory<*, *>
}
