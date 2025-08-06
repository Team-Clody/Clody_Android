package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.local.datasource.AppReviewLocalDataSource
import com.sopt.clody.domain.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val appReviewLocalDataSource: AppReviewLocalDataSource,
) : ReviewRepository {
    override fun getShouldShowPopup(): Boolean = appReviewLocalDataSource.shouldShowPopup

    override fun setShouldShowPopup(state: Boolean) {
        appReviewLocalDataSource.shouldShowPopup = state
    }
}
