package com.sopt.clody.presentation.ui.setting.screen

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator

@Composable
fun WebViewRoute(
    navigator: SettingNavigator,
    encodeUrl: String
) {
    WebViewScreen(
        encodeUrl = encodeUrl,
        onClickBack = { navigator.navigateBack() }
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    encodeUrl: String,
    onClickBack: () -> Unit
) {
    var webView: WebView? by remember { mutableStateOf(null) }
    val canGoBack by remember { derivedStateOf { webView?.canGoBack() ?: false } }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            allowFileAccess = true
                            allowContentAccess = true
                            javaScriptCanOpenWindowsAutomatically = true
                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        }
                        loadUrl(encodeUrl)
                        webView = this
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }
    )

    BackHandler(enabled = canGoBack) {
        if (canGoBack) {
            webView?.goBack()
        } else {
            onClickBack()
        }
    }
}

