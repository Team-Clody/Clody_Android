package com.sopt.clody.domain.repository

interface AdRepository {
    suspend fun startAd(year: Int, month: Int, day: Int): Result<Unit>
    suspend fun endAd(year: Int, month: Int, day: Int): Result<Unit>
    suspend fun loadRewardedAd(): Result<Unit>
}
