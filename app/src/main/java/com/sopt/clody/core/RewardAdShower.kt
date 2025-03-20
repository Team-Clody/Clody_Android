package com.sopt.clody.core

import android.app.Activity

interface RewardAdShower {
    fun showAd(activity: Activity, onAdRewarded: () -> Unit, onAdDismissed: () -> Unit)
}
