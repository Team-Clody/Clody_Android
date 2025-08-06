package com.sopt.clody.presentation.utils.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * [LifecycleOwner]의 생명주기가 [Lifecycle.State.STARTED] 상태 이상일 때만
 * 지정한 [block]을 실행하고, 그렇지 않으면 자동으로 중단.
 *
 * 내부적으로 [lifecycleScope]에서 코루틴을 실행하고,
 * [Lifecycle.repeatOnLifecycle]을 STARTED 상태 기준으로 래핑.
 *
 * @param block STARTED 상태에서 실행할 suspend 함수 블록
 *
 * @see Lifecycle.repeatOnLifecycle
 * @see Lifecycle.State.STARTED
 */

fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

/**
 * [LifecycleOwner]가 [lifecycleState] 이상일 때만 [block]을 실행하는 [LaunchedEffect] 기반의 Composable 유틸함수.
 * 내부적으로 [Lifecycle.repeatOnLifecycle]을 사용하여 생명주기 안전성을 보장.
 * SideEffect 처리를 lifecycle-aware하게 실행하고 싶을 때 사용하면 됨.
 *
 * @param key [LaunchedEffect]를 트리거할 key. 일반적으로 의존성이 되는 상태나 객체.
 * @param lifecycleState 반복 실행을 시작할 최소 생명주기 상태 (기본값: STARTED)
 * @param block 지정한 생명주기 상태 이상일 때만 실행할 suspend 블록
 */

@Composable
fun LaunchedEffectWhenStarted(
    key: Any? = Unit,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(lifecycleState) {
            block()
        }
    }
}
