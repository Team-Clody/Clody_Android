package com.sopt.clody.data.ad

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.sopt.clody.core.ad.RewardAdShower
import com.sopt.clody.data.remote.datasource.AdRemoteDataSource
import javax.inject.Inject

class RewardAdShowerImpl @Inject constructor(
    private val adRemoteDataSource: AdRemoteDataSource,
) : RewardAdShower {
    override fun showAd(activity: Activity, onAdRewarded: () -> Unit, onAdDismissed: () -> Unit) {
        val rewardedAd = adRemoteDataSource.getRewardedAd()
        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    onAdDismissed()
                }

                override fun onAdShowedFullScreenContent() {}
            }

            ad.show(activity) {
                onAdRewarded()
            }
        } ?: onAdDismissed()
    }
}
