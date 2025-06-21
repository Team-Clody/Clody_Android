package com.sopt.clody.core.security.login

import android.content.Context

interface LoginSecurityChecker {
    fun isDeviceRooted(): Boolean
    fun isChromeInstalled(context: Context): Boolean
}
