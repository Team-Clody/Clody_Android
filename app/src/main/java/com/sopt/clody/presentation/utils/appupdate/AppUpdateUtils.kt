package com.sopt.clody.presentation.utils.appupdate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

object AppUpdateUtils {

    /**
     * 마켓 이동
     * @param context Context
     */
    fun navigateToMarket(context: Context) {
        val packageName = context.packageName
        val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (marketIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(marketIntent)
        } else {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        }
    }

    /**
     * 마켓 이동 후 앱 종료 (Hard Update 용)
     * @param activity Activity
     */
    fun navigateToMarketAndFinish(activity: Activity) {
        navigateToMarket(activity)
        activity.finishAffinity()
    }
}
