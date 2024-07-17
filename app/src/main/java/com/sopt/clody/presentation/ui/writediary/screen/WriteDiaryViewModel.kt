package com.sopt.clody.presentation.ui.writediary.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.repository.WriteDiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteDiaryViewModel @Inject constructor(
    private val writeDiaryRepository: WriteDiaryRepository
) : ViewModel() {

    private val _writeDiaryState = MutableStateFlow<WriteDiaryState>(WriteDiaryState.Idle)
    val writeDiaryState: StateFlow<WriteDiaryState> = _writeDiaryState

    fun writeDiary(year: Int, month: Int, day: Int, contents: List<String>) {
        _writeDiaryState.value = WriteDiaryState.Loading
        viewModelScope.launch {
            val date = String.format("%04d-%02d-%02d", year, month, day)
            val result = writeDiaryRepository.writeDiary(date, contents)
            _writeDiaryState.value = result.fold(
                onSuccess = { response ->
                    response.createdAt.let {
                        WriteDiaryState.Success(it)
                    } ?: WriteDiaryState.Failure("createdAt field is missing")
                },
                onFailure = { WriteDiaryState.Failure(it.message ?: "Unknown error") }
            )
        }
    }
}
