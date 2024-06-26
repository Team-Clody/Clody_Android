package com.sopt.clody.presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.utils.extension.showToast
import com.sopt.core.view.UiState

@Composable
fun ExampleScreen(viewModel: ExampleViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is UiState.Loading -> {
            // 로딩 중 화면
        }
        is UiState.Success -> {
            val data = (uiState as UiState.Success).data
            // 성공 화면
            Surface {
                showToast(message = "성공")
            }
        }
        is UiState.Failure -> {
            val message = (uiState as UiState.Failure).msg
            // 실패 화면
        }
        is UiState.Empty -> {
            // 빈 화면
        }
    }
}
