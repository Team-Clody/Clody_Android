package com.sopt.clody.data.local.datasource

/**
 * 임시 저장 최초 사용 여부 판단을 위한 SharedPreferences
 * @property isDraftUsed 임시 저장 사용 여부
 * @property isFirstUse 임시 저장 최초 사용 여부
 */
interface FirstDraftLocalDataSource {
    var isDraftUsed: Boolean
    var isFirstUse: Boolean
}
