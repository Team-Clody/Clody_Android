package com.sopt.clody.presentation.utils.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
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
