package com.sopt.clody.presentation.utils.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

fun <T> Flow<T>.throttleFirst(throttleTimeMillis: Long): Flow<T> {
    var lastEmissionTime = 0L
    return this.filter { // .filter을 통해 조건에 맞는 이벤트만 통과시킨다.
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime >= throttleTimeMillis) {
            lastEmissionTime = currentTime
            true
        } else {
            false
        }
    }
}
