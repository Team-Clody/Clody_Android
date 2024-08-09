package com.sopt.clody.presentation.ui.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun FailureDialog(message: String = "일시적인 오류가 발생했어요.\n잠시 후 다시 시도해주세요.") {
    Dialog(
        onDismissRequest = { /*TODO*/ },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .wrapContentSize(Alignment.Center)
                .padding(horizontal = 24.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ClodyTheme.colors.white),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .height(194.dp)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_failure_dialog),
                        contentDescription = null,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(
                        text = message,
                        color = ClodyTheme.colors.gray04,
                        style = ClodyTheme.typography.body3Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(ClodyTheme.colors.mainYellow)
                    ) {
                        Text(
                            text = stringResource(R.string.failure_dialog_confirm_btn),
                            color = ClodyTheme.colors.gray02,
                            style = ClodyTheme.typography.body3SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun showFailureDialog() {
    FailureDialog()
}
