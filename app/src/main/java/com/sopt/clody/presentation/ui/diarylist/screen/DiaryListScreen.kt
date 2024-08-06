import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.component.timepicker.YearMonthPicker
import com.sopt.clody.presentation.ui.diarylist.component.DiaryListTopAppBar
import com.sopt.clody.presentation.ui.diarylist.component.MonthlyDiaryList
import com.sopt.clody.presentation.ui.diarylist.navigation.DiaryListNavigator
import com.sopt.clody.presentation.ui.diarylist.screen.DeleteDiaryListState
import com.sopt.clody.presentation.ui.diarylist.screen.DiaryListState
import com.sopt.clody.presentation.ui.diarylist.screen.DiaryListViewModel
import com.sopt.clody.presentation.utils.extension.showToast
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DiaryListRoute(
    navigator: DiaryListNavigator,
    diaryListViewModel: DiaryListViewModel = hiltViewModel(),
    selectedYearFromHome: Int,
    selectedMonthFromHome: Int
) {
    DiaryListScreen(
        diaryListViewModel = diaryListViewModel,
        selectedYearFromHome = selectedYearFromHome,
        selectedMonthFromHome = selectedMonthFromHome,
        onClickCalendar = { selectedYearFromDiaryList, selectedMonthFromDiaryList -> navigator.navigateHome(selectedYearFromDiaryList, selectedMonthFromDiaryList) },
        onClickReplyDiary = { year, month, day -> navigator.navigateReplyLoading(year, month, day) }
    )
}

@Composable
fun DiaryListScreen(
    diaryListViewModel: DiaryListViewModel,
    selectedYearFromHome: Int,
    selectedMonthFromHome: Int,
    onClickCalendar: (Int, Int) -> Unit,
    onClickReplyDiary: (Int, Int, Int) -> Unit,
) {
    var showYearMonthPickerState by remember { mutableStateOf(false) }
    var selectedYearInDiaryList by remember { mutableIntStateOf(selectedYearFromHome) }
    var selectedMonthInDiaryList by remember { mutableIntStateOf(selectedMonthFromHome) }
    val diaryListState by diaryListViewModel.diaryListState.collectAsState()
    val deleteDiaryResult by diaryListViewModel.deleteDiaryResult.collectAsState()

    val onYearMonthSelected: (Int, Int) -> Unit = { year, month ->
        selectedYearInDiaryList = year
        selectedMonthInDiaryList = month
    }

    LaunchedEffect(selectedYearInDiaryList, selectedMonthInDiaryList) {
        diaryListViewModel.fetchMonthlyDiary(selectedYearInDiaryList, selectedMonthInDiaryList)
    }

    when (deleteDiaryResult) {
        is DeleteDiaryListState.Loading -> {
        }

        is DeleteDiaryListState.Success -> {
            LaunchedEffect(Unit) {
                diaryListViewModel.fetchMonthlyDiary(selectedYearInDiaryList, selectedMonthInDiaryList)
            }
        }

        is DeleteDiaryListState.Failure -> {
        }

        else -> {}
    }

    Scaffold(
        topBar = {
            Column {
                DiaryListTopAppBar(
                    onClickCalendar = { onClickCalendar(selectedYearInDiaryList, selectedMonthInDiaryList) },
                    selectedYear = selectedYearInDiaryList,
                    selectedMonth = selectedMonthInDiaryList,
                    onShowYearMonthPickerStateChange = { newState -> showYearMonthPickerState = newState }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ClodyTheme.colors.gray07)
                )
            }
        },
        containerColor = ClodyTheme.colors.gray08,
        content = { innerPadding ->
            when (diaryListState) {
                is DiaryListState.Idle -> {}
                is DiaryListState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(innerPadding)
                                .wrapContentSize(Alignment.Center),
                            color = ClodyTheme.colors.mainYellow
                        )
                    }
                }

                is DiaryListState.Success -> {
                    MonthlyDiaryList(
                        paddingValues = innerPadding,
                        onClickReplyDiary = onClickReplyDiary,
                        diaries = (diaryListState as DiaryListState.Success).data.diaries,
                        diaryListViewModel = diaryListViewModel
                    )
                }

                is DiaryListState.Failure -> {
                    showToast(message = "${(diaryListState as DiaryListState.Failure).errorMessage}")
                }
            }
        }
    )

    if (showYearMonthPickerState) {
        ClodyPopupBottomSheet(onDismissRequest = { showYearMonthPickerState = false }) {
            YearMonthPicker(
                onDismissRequest = { showYearMonthPickerState = false },
                selectedYear = selectedYearInDiaryList,
                selectedMonth = selectedMonthInDiaryList,
                onYearMonthSelected = onYearMonthSelected
            )
        }
    }
}
