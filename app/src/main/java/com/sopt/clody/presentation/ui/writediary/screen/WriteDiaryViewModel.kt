package com.sopt.clody.presentation.ui.writediary.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.util.NetworkUtil
import com.sopt.clody.domain.repository.DiaryRepository
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.UNKNOWN_ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteDiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    private val _writeDiaryState = MutableStateFlow<WriteDiaryState>(WriteDiaryState.Idle)
    val writeDiaryState: StateFlow<WriteDiaryState> = _writeDiaryState

    private val _showFailureDialog = MutableStateFlow(false)
    val showFailureDialog: StateFlow<Boolean> = _showFailureDialog

    private val _failureMessage = MutableStateFlow("")
    val failureMessage: StateFlow<String> = _failureMessage

    private val _entries = mutableStateListOf("")
    val entries: List<String> get() = _entries

    private val _showWarnings = mutableStateListOf(false)
    val showWarnings: List<Boolean> get() = _showWarnings

    var showLimitMessage by mutableStateOf(false)
        private set

    var showEmptyFieldsMessage by mutableStateOf(false)
        private set

    var showDeleteBottomSheet by mutableStateOf(false)
        private set

    var entryToDelete by mutableIntStateOf(-1)
        private set

    var showDialog by mutableStateOf(false)
        private set

    fun writeDiary(year: Int, month: Int, day: Int, contents: List<String>) {
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _failureMessage.value = FAILURE_NETWORK_MESSAGE
                _showFailureDialog.value = true
                return@launch
            }

            _writeDiaryState.value = WriteDiaryState.Loading
            val date = String.format("%04d-%02d-%02d", year, month, day)
            val result = diaryRepository.writeDiary(date, contents)
            _writeDiaryState.value = result.fold(
                onSuccess = { response ->
                    when (response.replyType) {
                        "DELETED" -> WriteDiaryState.NoReply
                        else -> WriteDiaryState.Success(response.createdAt)
                    }
                },
                onFailure = {
                    _failureMessage.value = if (it.message?.contains("200") == false) {
                        FAILURE_TEMPORARY_MESSAGE
                    } else {
                        it.localizedMessage ?: UNKNOWN_ERROR
                    }
                    _showFailureDialog.value = true
                    WriteDiaryState.Failure(_failureMessage.value)
                }
            )
        }
    }

    fun resetFailureDialog() {
        _showFailureDialog.value = false
        _failureMessage.value = ""
        updateShowDialog(false)
    }

    fun addEntry() {
        if (_entries.size < MAX_ENTRIES) {
            _entries.add("")
            _showWarnings.add(false)
            checkLimitMessage()
        } else {
            showLimitMessage = true
        }
    }

    fun removeEntry(index: Int) {
        if (index in _entries.indices) {
            _entries.removeAt(index)
            _showWarnings.removeAt(index)
            checkLimitMessage()
        }
    }

    fun updateEntry(index: Int, newText: String) {
        if (index in _entries.indices) {
            _entries[index] = newText
            validateEntry(index, newText)
        }
    }

    fun validateEntries() {
        for (i in _entries.indices) {
            validateEntry(i, _entries[i])
        }
        checkEmptyFieldsMessage()
    }

    fun validateEntry(index: Int, text: String) {
        if (index in _entries.indices) {
            _showWarnings[index] = !isValidEntry(text)
        }
    }

    private fun isValidEntry(text: String): Boolean {
        val textWithoutSpaces = text.replace("\\s".toRegex(), "")
        return textWithoutSpaces.matches(Regex(ENTRY_REGEX))
    }

    private fun checkLimitMessage() {
        showLimitMessage = _entries.size == 5
    }

    private fun checkEmptyFieldsMessage() {
        showEmptyFieldsMessage = _entries.size > 1 && _entries.any { it.isEmpty() }
    }

    fun updateShowDialog(show: Boolean) {
        showDialog = show
    }

    fun updateShowDeleteBottomSheet(show: Boolean) {
        showDeleteBottomSheet = show
    }

    fun updateShowLimitMessage(show: Boolean) {
        showLimitMessage = show
    }

    fun updateShowEmptyFieldsMessage(show: Boolean) {
        showEmptyFieldsMessage = show
    }

    fun setEntryToDeleteIndex(index: Int) {
        entryToDelete = index
    }

    companion object {
        const val MAX_ENTRIES = 5
        const val ENTRY_REGEX = "^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣\\W]{2,50}$"
    }
}
