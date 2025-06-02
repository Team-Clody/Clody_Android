package com.sopt.clody.core.login

import android.content.Context

interface LoginSdk {
    suspend fun login(context: Context): Result<LoginAccessToken>
    suspend fun logout(): Result<Unit>
    suspend fun unlink(): Result<Unit>
}
