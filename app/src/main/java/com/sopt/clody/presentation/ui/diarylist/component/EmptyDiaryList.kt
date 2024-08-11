package com.sopt.clody.presentation.ui.diarylist.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun EmptyDiaryList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text= stringResource(R.string.diary_list_empty_diary_list),
            color = ClodyTheme.colors.gray06,
            textAlign = TextAlign.Center,
            style = ClodyTheme.typography.body2SemiBold
        )
    }
}
