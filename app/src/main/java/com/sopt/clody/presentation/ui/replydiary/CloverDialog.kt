package com.sopt.clody.presentation.ui.replydiary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun CloverDialog(
    onDismiss: () -> Unit,
    titleMassage: String,
    descriptionMassage: String,
    confirmOption: String,
    confirmAction: () -> Unit,
    confirmButtonColor: Color,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .wrapContentSize(Alignment.Center)
                .padding(horizontal = 50.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ClodyTheme.colors.white),
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dialog_clover),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )

                    Text(
                        text = titleMassage,
                        style = ClodyTheme.typography.detail1Medium,
                        color = ClodyTheme.colors.gray05,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = descriptionMassage,
                        style = ClodyTheme.typography.head3,
                        color = ClodyTheme.colors.gray01,
                        modifier = Modifier.padding(bottom = 30.dp)
                    )

                    Text(
                        text = confirmOption,
                        style = ClodyTheme.typography.body2SemiBold,
                        color = ClodyTheme.colors.mainYellow,
                        modifier = Modifier.clickable(onClick = confirmAction)
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCloverDialog() {
    CloverDialog(
        onDismiss = { },
        titleMassage = "클로버를 획득했어요!",
        descriptionMassage = "",
        confirmOption = "확인",
        confirmAction = { },
        confirmButtonColor = ClodyTheme.colors.mainYellow
    )
}
