package com.sopt.clody.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.domain.model.ExampleModel
import com.sopt.clody.domain.usecase.GetAllExamplesUseCase
import com.sopt.clody.utils.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val getAllExamplesUseCase: GetAllExamplesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ExampleModel>>>(UiState.Empty)
    val uiState: StateFlow<UiState<List<ExampleModel>>> = _uiState

    private val _errorFlow = MutableSharedFlow<Throwable?>()
    val errorFlow: SharedFlow<Throwable?> = _errorFlow

    init {
        getExampleData()
    }

    private fun getExampleData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = getAllExamplesUseCase()
            if (result.isSuccess) {
                _uiState.value = UiState.Success(result.getOrNull().orEmpty())
            } else {
                val exception = result.exceptionOrNull()
                _uiState.value = UiState.Failure(exception?.message ?: "Unknown error")
                _errorFlow.emit(exception)
            }
        }
    }
}

