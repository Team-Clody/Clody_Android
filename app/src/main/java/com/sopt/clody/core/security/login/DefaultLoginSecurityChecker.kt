package com.sopt.clody.core.security.login

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.io.File
import javax.inject.Inject

/**
 * 기본 보안 점검 구현체.
 *
 * 디바이스 루팅 여부 및 Chrome 브라우저 설치 여부를 확인하는 기능 구현.
 *
 */

class DefaultLoginSecurityChecker @Inject constructor() : LoginSecurityChecker {

    /**
     * 디바이스가 루팅되었는지 여부를 검사.
     * Step
     * - `Build.TAGS`에 `test-keys`가 포함되어 있는지 확인
     * - 루팅에 사용되는 바이너리 또는 앱의 존재 여부 검사
     * - `which su` 명령어 실행 결과를 통해 `su` 명령어의 존재 여부 확인
     *
     * @return 디바이스가 루팅되었다면 `true`, 그렇지 않다면 `false`
     */
    override fun isDeviceRooted(): Boolean {
        val buildTags = Build.TAGS
        if (buildTags != null && buildTags.contains("test-keys")) return true

        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su", "/system/bin/su", "/system/xbin/su",
            "/data/local/xbin/su", "/data/local/bin/su",
            "/system/sd/xbin/su", "/system/bin/failsafe/su",
            "/data/local/su",
        )
        if (paths.any { File(it).exists() }) return true

        return try {
            Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
                .inputStream.bufferedReader().readLine() != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 디바이스에 Chrome 브라우저가 설치되어 있는지 여부
     *
     * `com.android.chrome` 패키지의 존재 여부로 Chrome 설치 여부를 판단.
     *
     * @return Chrome이 설치되어 있다면 `true`, 아니라면 `false`
     */
    override fun isChromeInstalled(context: Context): Boolean = try {
        context.packageManager.getPackageInfo("com.android.chrome", 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
