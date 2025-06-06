package com.sopt.clody.core.review

import android.app.Activity
import com.google.android.play.core.review.ReviewManagerFactory
import com.sopt.clody.presentation.utils.appupdate.AppUpdateUtils
import timber.log.Timber

object InAppReviewManager {
    fun showPopup(activity: Activity) {
        if (activity.isFinishing || activity.isDestroyed) return

        val reviewManager = ReviewManagerFactory.create(activity)
        val request = reviewManager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                reviewManager.launchReviewFlow(activity, reviewInfo)
            } else {
                try {
                    AppUpdateUtils.navigateToMarket(activity)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Timber.e(e, "Failed to open store for app review")
                }
            }
        }
    }
}
