package com.sopt.clody.presentation.ui.component.timepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class PickerState {
    var selectedItem: String by mutableStateOf("")
}

@Composable
fun rememberPickerState() = remember { PickerState() }
