package com.sopt.clody.presentation.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.sopt.clody.data.local.repository.AppUpdateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppUpdateViewModel @Inject constructor(
    private val appUpdateRepository: AppUpdateRepository
) : ViewModel() {

    private val _appUpdateInfo = MutableSharedFlow<AppUpdateInfo?>()
    val appUpdateInfo: SharedFlow<AppUpdateInfo?> = _appUpdateInfo.asSharedFlow()

    fun checkForAppUpdate() {
        viewModelScope.launch {
            appUpdateRepository.checkForUpdate { updateInfo ->
                viewModelScope.launch {
                    _appUpdateInfo.emit(updateInfo)
                }
            }
        }
    }
}
