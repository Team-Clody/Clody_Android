package com.sopt.clody.data.local.datasourceimpl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.sopt.clody.data.local.datasource.AppReviewLocalDataSource
import com.sopt.clody.di.qualifier.ReviewPrefs
import javax.inject.Inject

class AppReviewLocalDataSourceImpl @Inject constructor(
    @ReviewPrefs private val sharedPreferences: SharedPreferences,
) : AppReviewLocalDataSource {

    override var shouldShowPopup: Boolean
        get() = sharedPreferences.getBoolean(SHOULD_SHOW_POPUP, true)
        set(value) = sharedPreferences.edit { putBoolean(SHOULD_SHOW_POPUP, value) }

    companion object {
        private const val SHOULD_SHOW_POPUP = "shouldShowPopup"
    }
}
