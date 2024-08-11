package com.sopt.clody.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun FailureScreen(
    message: String = "일시적인 오류가 발생했어요.\n잠시 후 다시 시도해주세요.",
    confirmAction: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.img_failure_screen_clody),
            contentDescription = null,
            modifier = Modifier.padding(start = 20.dp, bottom = 30.dp)
        )
        Text(
            text = message,
            color = ClodyTheme.colors.gray06,
            textAlign = TextAlign.Center,
            style = ClodyTheme.typography.body2SemiBold
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = confirmAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(ClodyTheme.colors.mainYellow)
        ) {
            Text(
                text = stringResource(R.string.failure_screen_refresh_btn),
                modifier = Modifier.padding(6.dp),
                color = ClodyTheme.colors.gray01,
                style = ClodyTheme.typography.body2SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun showFailureScreen() {
    FailureScreen()
}
