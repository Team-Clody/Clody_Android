package com.sopt.clody.core.review

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.review.ReviewManagerFactory
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
                    val uri = Uri.parse("market://details?id=${activity.packageName}")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    if (intent.resolveActivity(activity.packageManager) != null) {
                        activity.startActivity(intent)
                    } else {
                        val webUri = Uri.parse("https://play.google.com/store/apps/details?id=${activity.packageName}")
                        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                        activity.startActivity(webIntent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Timber.e(e, "Failed to open store for app review")
                }
            }
        }
    }
}
