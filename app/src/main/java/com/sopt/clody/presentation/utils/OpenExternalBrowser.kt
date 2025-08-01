package com.sopt.clody.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openExternalBrowser(context: Context, url: String) {
    val uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    // 웹 브라우저 앱이 설치되어 있지 않은 경우
    context.packageManager.resolveActivity(intent, 0)?.let {
        context.startActivity(intent)
    } ?: return
}
