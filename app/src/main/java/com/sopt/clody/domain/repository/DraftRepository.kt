package com.sopt.clody.domain.repository

interface DraftRepository {
    fun getIsDraftUsed(): Boolean
    fun setIsDraftUsed(state: Boolean)
    fun getIsFirstUse(): Boolean
    fun setIsFirstUse(state: Boolean)
}
