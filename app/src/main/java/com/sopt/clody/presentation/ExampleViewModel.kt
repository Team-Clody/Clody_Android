package com.sopt.clody.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.domain.model.ExampleModel
import com.sopt.clody.domain.repository.ExampleRepository
import com.sopt.core.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val exampleRepository: ExampleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<ExampleModel>>>(UiState.Empty)
    val uiState: StateFlow<UiState<List<ExampleModel>>> = _uiState

    init {
        getExampleData()
    }

    private fun getExampleData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val data = exampleRepository.getAllExamples()
                _uiState.value = UiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = UiState.Failure(e.message ?: "Unknown error")
            }
        }
    }
}
