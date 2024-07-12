package com.sopt.clody.presentation.ui.auth.component.container

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun PickerBox(
    time: String, // 추가: 선택된 시간을 받는 매개변수
    modifier: Modifier = Modifier,
    onClick: () -> Unit // onClick 파라미터 추가
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                indication = null, // 클릭 효과 제거
                interactionSource = interactionSource
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = time, // 수정: 선택된 시간을 표시
                style = ClodyTheme.typography.body1SemiBold,
                color = ClodyTheme.colors.gray03,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_timereminder_down),
                contentDescription = null,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPickerBox() {
    PickerBox(time = "오후 6:00") {

    }
}
