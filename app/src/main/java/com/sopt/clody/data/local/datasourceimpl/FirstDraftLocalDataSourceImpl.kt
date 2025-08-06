package com.sopt.clody.data.local.datasourceimpl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.sopt.clody.data.local.datasource.FirstDraftLocalDataSource
import com.sopt.clody.di.qualifier.FirstDraftPrefs
import javax.inject.Inject

class FirstDraftLocalDataSourceImpl @Inject constructor(
    @FirstDraftPrefs private val sharedPreferences: SharedPreferences,
) : FirstDraftLocalDataSource {
    override var isDraftUsed: Boolean
        get() = sharedPreferences.getBoolean(IS_DRAFT_USED, false)
        set(value) = sharedPreferences.edit { putBoolean(IS_DRAFT_USED, value) }

    override var isFirstUse: Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST_USE, false)
        set(value) = sharedPreferences.edit { putBoolean(IS_FIRST_USE, value) }

    companion object {
        private const val IS_DRAFT_USED = "IS_DRAFT_USED"
        private const val IS_FIRST_USE = "IS_FIRST_USE"
    }
}
