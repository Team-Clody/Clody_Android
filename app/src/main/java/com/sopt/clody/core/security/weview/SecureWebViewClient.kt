package com.sopt.clody.core.security.weview

import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class SecureWebViewClient(
    private val context: Context,
    private val allowedDomains: List<String> = listOf("notion.so", "forms.gle"),
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val host = request.url.host ?: return true
        val isSafeDomain = allowedDomains.any { host.contains(it) }

        return if (isSafeDomain) {
            false
        } else {
            Intent(Intent.ACTION_VIEW, request.url).let {
                context.startActivity(it)
            }
            true
        }
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        handler.cancel()
    }
}
