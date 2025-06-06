package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.local.datasource.FirstDraftLocalDataSource
import com.sopt.clody.domain.repository.DraftRepository
import javax.inject.Inject

class DraftRepositoryImpl @Inject constructor(
    private val firstDraftLocalDataSource: FirstDraftLocalDataSource,
) : DraftRepository {
    override fun getIsDraftUsed(): Boolean = firstDraftLocalDataSource.isDraftUsed

    override fun setIsDraftUsed(state: Boolean) {
        firstDraftLocalDataSource.isDraftUsed = state
    }

    override fun getIsFirstUse(): Boolean = firstDraftLocalDataSource.isFirstUse

    override fun setIsFirstUse(state: Boolean) {
        firstDraftLocalDataSource.isFirstUse = state
    }
}
