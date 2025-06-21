package com.sopt.clody.presentation.utils.appupdate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

object AppUpdateUtils {

    private const val PLAY_STORE_PACKAGE = "com.android.vending"
    private const val MARKET_URI_PREFIX = "market://details?id="
    private const val WEB_URI_PREFIX = "https://play.google.com/store/apps/details?id="

    /**
     * 마켓 이동
     * @param context Context
     */
    fun navigateToMarket(context: Context) {
        val packageName = context.packageName

        val marketIntent = Intent(Intent.ACTION_VIEW, "$MARKET_URI_PREFIX$packageName".toUri()).apply {
            setPackage(PLAY_STORE_PACKAGE)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val webIntent = Intent(Intent.ACTION_VIEW, "$WEB_URI_PREFIX$packageName".toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        runCatching {
            val safeIntent = if (marketIntent.resolveActivity(context.packageManager) != null) {
                marketIntent
            } else {
                webIntent
            }
            context.startActivity(safeIntent)
        }.onFailure {
            // 예외 상황 처리 (마켓 앱도, 브라우저도 없는 극단적 상황 과연 있을까?)
            Log.e("AppUpdateUtils", "Failed to open market", it)
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
