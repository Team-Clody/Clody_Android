package com.sopt.clody

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils.initAmplitude
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ClodyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initKakaoSdk()
        FirebaseApp.initializeApp(this)
        initAmplitude(applicationContext)
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}
