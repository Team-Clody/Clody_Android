package com.sopt.clody.presentation.ui.writediary.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.core.network.NetworkConnectivityObserver
import com.sopt.clody.core.network.NetworkStatus
import com.sopt.clody.domain.repository.DiaryRepository
import com.sopt.clody.domain.repository.DraftRepository
import com.sopt.clody.domain.usecase.FetchDraftDiaryUseCase
import com.sopt.clody.domain.usecase.SaveDraftDiaryUseCase
import com.sopt.clody.presentation.utils.language.LanguageProvider
import com.sopt.clody.presentation.utils.network.ErrorMessageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WriteDiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val fetchDraftDiaryUseCase: FetchDraftDiaryUseCase,
    private val saveDraftDiaryUseCase: SaveDraftDiaryUseCase,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val draftRepository: DraftRepository,
    private val languageProvider: LanguageProvider,
    private val errorMessageProvider: ErrorMessageProvider,
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

    var showExitDialog by mutableStateOf(false)
        private set

    private var initialEntries: List<String> = emptyList()

    private val _diaryMaxLength = MutableStateFlow(languageProvider.getDiaryMaxLength())
    val diaryMaxLength: StateFlow<Int> = _diaryMaxLength

    fun writeDiary(year: Int, month: Int, day: Int, contents: List<String>) {
        viewModelScope.launch {
            if (networkConnectivityObserver.networkStatus.first() == NetworkStatus.Unavailable) {
                _failureMessage.value = errorMessageProvider.getNetworkError()
                _showFailureDialog.value = true
                return@launch
            }

            _writeDiaryState.value = WriteDiaryState.Loading
            val date = String.format("%04d-%02d-%02d", year, month, day)
            val result = diaryRepository.writeDiary(date, contents)
            _writeDiaryState.value = result.fold(
                onSuccess = { response ->
                    if (isDiaryExpired(year, month, day)) {
                        WriteDiaryState.NoReply
                    } else {
                        when (response.replyType) {
                            "DELETED" -> WriteDiaryState.NoReply
                            else -> WriteDiaryState.Success(response.createdAt)
                        }
                    }
                },
                onFailure = {
                    _failureMessage.value = if (it.message?.contains("200") == false) {
                        errorMessageProvider.getTemporaryError()
                    } else {
                        it.localizedMessage ?: errorMessageProvider.getUnknownError()
                    }
                    _showFailureDialog.value = true
                    WriteDiaryState.Failure(_failureMessage.value)
                },
            )
        }
    }

    private fun isDiaryExpired(year: Int, month: Int, day: Int): Boolean {
        val diaryDate = LocalDate.of(year, month, day)
        val yesterday = LocalDate.now().minusDays(1)
        return diaryDate.isBefore(yesterday)
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
        return textWithoutSpaces.matches(Regex("^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣\\W]{2,${_diaryMaxLength.value}}$"))
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

    fun updateShowExitDialog(show: Boolean) {
        showExitDialog = show
    }

    fun hasChangedFromInitial(): Boolean {
        if (initialEntries.isEmpty()) return false
        val current = entries.map { it.trim() }
        val initial = initialEntries.map { it.trim() }
        return current != initial
    }

    fun fetchDraftDiary(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            _entries.clear()
            _showWarnings.clear()

            val result = fetchDraftDiaryUseCase(year, month, day)
            result.onSuccess { response ->
                val drafts = response.draftDiaries.ifEmpty { listOf("") }
                _entries.addAll(drafts)
                initialEntries = drafts.toList()

                _showWarnings.addAll(List(_entries.size) { false })
                checkLimitMessage()
                checkEmptyFieldsMessage()
            }.onFailure {
                ensureDefaultEntry()
                _failureMessage.value = errorMessageProvider.getFetchTempDiaryFailedError()
                _showFailureDialog.value = true
            }
        }
    }

    fun saveDraftDiary(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            val date = String.format("%04d-%02d-%02d", year, month, day)
            val result = saveDraftDiaryUseCase(date, _entries.toList())
            result.onSuccess {
                _failureMessage.value = ""
                _showFailureDialog.value = false
            }.onFailure { e ->
                _failureMessage.value = e.localizedMessage ?: errorMessageProvider.getUnknownError()
                _showFailureDialog.value = true
            }
        }
    }

    private fun ensureDefaultEntry() {
        _entries.clear()
        _entries.add("")
        _showWarnings.clear()
        _showWarnings.add(false)
        checkLimitMessage()
        checkEmptyFieldsMessage()
    }

    fun updateDraftUsage() {
        if (!draftRepository.getIsDraftUsed()) {
            draftRepository.setIsDraftUsed(true)
            draftRepository.setIsFirstUse(true)
        }
    }

    companion object {
        const val MAX_ENTRIES = 5
    }
}
