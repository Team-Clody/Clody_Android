package com.sopt.clody.domain.usecase

import com.sopt.clody.domain.repository.AdRepository
import javax.inject.Inject

class LoadRewardedAdUseCase @Inject constructor(
    private val repository: AdRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.loadRewardedAd()
    }
}
