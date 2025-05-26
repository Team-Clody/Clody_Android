package com.sopt.clody.presentation.utils.base

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.sopt.clody.ui.theme.ClodyTheme

// 아래 폴드는 예시이고 fontScale 같은 값도 조정이 가능합니다.
//@Preview(name = "Galaxy Z Fold3 접힌화면 (840x2289)", widthDp = 320, heightDp = 870, showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@PreviewScreenSizes
annotation class ClodyPreview

@Composable
fun BasePreview(content: @Composable () -> Unit = {}) {
    ClodyTheme {
        Surface(color = ClodyTheme.colors.white) {
            content()
        }
    }
}
