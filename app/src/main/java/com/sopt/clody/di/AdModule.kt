package com.sopt.clody.di

import com.sopt.clody.core.RewardAdShower
import com.sopt.clody.data.ad.RewardAdShowerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AdModule {
    @Binds
    abstract fun bindRewardAdShower(
        impl: RewardAdShowerImpl,
    ): RewardAdShower
}
