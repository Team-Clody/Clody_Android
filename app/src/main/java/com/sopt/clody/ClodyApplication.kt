package com.sopt.clody

import android.app.Application
import co.ab180.airbridge.Airbridge
import co.ab180.airbridge.AirbridgeOptionBuilder
import com.airbnb.mvrx.Mavericks
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils.initAmplitude
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ClodyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initKakaoSdk()
        FirebaseApp.initializeApp(this)
        Mavericks.initialize(this)
        initAmplitude(applicationContext)
        initAirBridge()
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }

    private fun initAirBridge() {
        val option = AirbridgeOptionBuilder("clody", "3ba2277abcd044f29356dc0ee32165ff").build()
        Airbridge.initializeSDK(this, option)
    }
}
