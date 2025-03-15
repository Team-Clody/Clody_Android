package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.AdRemoteDataSource
import com.sopt.clody.domain.repository.AdRepository
import javax.inject.Inject

class AdRepositoryImpl @Inject constructor(
    private val adRemoteDataSource: AdRemoteDataSource
) : AdRepository {
    override suspend fun startAd(year: Int, month: Int, day: Int): Result<Unit> {
        return adRemoteDataSource.startAd(year, month, day)
    }

    override suspend fun endAd(year: Int, month: Int, day: Int): Result<Unit> {
        return adRemoteDataSource.endAd(year, month, day)
    }

    override suspend fun loadRewardedAd(): Result<Unit> {
        return adRemoteDataSource.loadRewardedAd()
    }
}
