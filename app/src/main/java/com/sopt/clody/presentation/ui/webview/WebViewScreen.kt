package com.sopt.clody.presentation.ui.webview

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.sopt.clody.BuildConfig
import com.sopt.clody.core.security.weview.SecureWebViewClient

@Composable
fun WebViewRoute(
    navigateToPrevious: () -> Unit,
    encodedUrl: String,
) {
    WebViewScreen(
        encodedUrl = encodedUrl,
        onClickBack = navigateToPrevious,
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    encodedUrl: String,
    onClickBack: () -> Unit,
) {
    val decodedUrl = remember(encodedUrl) {
        Uri.decode(encodedUrl)
    }

    var webView: WebView? by remember { mutableStateOf(null) }
    val canGoBack by remember { derivedStateOf { webView?.canGoBack() ?: false } }

    val allowedDomains = BuildConfig.ALLOWED_WEBVIEW_DOMAINS.split(",").map { it.trim() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = SecureWebViewClient(context, allowedDomains)
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            allowFileAccess = false
                            allowContentAccess = false
                            javaScriptCanOpenWindowsAutomatically = false
                            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                        }
                        loadUrl(decodedUrl)
                        webView = this
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        },
    )

    BackHandler(enabled = canGoBack) {
        if (canGoBack) {
            webView?.goBack()
        } else {
            onClickBack()
        }
    }
}
