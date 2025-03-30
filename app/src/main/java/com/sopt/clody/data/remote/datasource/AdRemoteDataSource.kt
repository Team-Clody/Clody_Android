package com.sopt.clody.data.remote.datasource

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sopt.clody.BuildConfig
import com.sopt.clody.data.remote.api.AdService
import com.sopt.clody.data.remote.dto.request.AdRequestDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class AdRemoteDataSource @Inject constructor(
    private val adService: AdService,
    @ApplicationContext private val context: Context,
) {
    private var rewardedAd: RewardedAd? = null
    private val adUnitId = BuildConfig.GOOGLE_ADMOB_UNIT_ID

    suspend fun startAd(year: Int, month: Int, day: Int): Result<Unit> {
        return runCatching {
            adService.startAd(AdRequestDto(year, month, day)).data
        }
    }

    suspend fun endAd(year: Int, month: Int, day: Int): Result<Unit> {
        return runCatching {
            adService.endAd(AdRequestDto(year, month, day)).data
        }
    }

    suspend fun loadRewardedAd(): Result<Unit> {
        return suspendCoroutine { continuation ->
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                context,
                adUnitId,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardedAd = ad
                        continuation.resume(Result.success(Unit))
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        continuation.resume(Result.failure(Exception(error.message)))
                    }
                },
            )
        }
    }

    fun getRewardedAd(): RewardedAd? {
        return rewardedAd
    }
}
