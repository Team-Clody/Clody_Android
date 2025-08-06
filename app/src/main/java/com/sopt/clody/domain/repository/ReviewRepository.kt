package com.sopt.clody.domain.repository

interface ReviewRepository {
    fun getShouldShowPopup(): Boolean
    fun setShouldShowPopup(state: Boolean)
}
