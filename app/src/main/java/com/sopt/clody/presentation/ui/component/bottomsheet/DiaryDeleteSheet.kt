package com.sopt.clody.presentation.ui.component.bottomsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DiaryDeleteSheet(
    onDismiss: () -> Unit,
    showDiaryDeleteDialog: () -> Unit
) {
    ClodyBottomSheet(
        onDismissRequest = onDismiss,
        content = {
            DiaryDeleteBottomSheetItem(
                onDismiss = onDismiss,
                showDiaryDeleteDialog = showDiaryDeleteDialog
            )
        }
    )
}

@Composable
fun DiaryDeleteBottomSheetItem(
    onDismiss: () -> Unit,
    showDiaryDeleteDialog: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ClodyTheme.colors.white)
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        onDismiss()
                        showDiaryDeleteDialog()
                    },
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }

                )
                .padding(start = 24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_bottomsheet_trash),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "삭제하기",
                style = ClodyTheme.typography.body4SemiBold,
                color = ClodyTheme.colors.gray01
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
        Spacer(modifier = Modifier.height(60.dp))
    }
}
