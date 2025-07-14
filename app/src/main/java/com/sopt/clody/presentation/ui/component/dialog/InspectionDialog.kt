package com.sopt.clody.presentation.ui.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sopt.clody.R
import com.sopt.clody.presentation.utils.base.BasePreview
import com.sopt.clody.presentation.utils.base.ClodyPreview
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun InspectionDialog(
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .wrapContentSize(Alignment.Center)
                .padding(horizontal = 24.dp),
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ClodyTheme.colors.white),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_inspection_dialog),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "보다 안정적인 클로디 서비스를 위해\n시스템 점검 중이에요. 곧 다시 만나요!",
                        color = ClodyTheme.colors.gray03,
                        textAlign = TextAlign.Center,
                        style = ClodyTheme.typography.body3Medium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "점검시간 : ",
                        color = ClodyTheme.colors.gray04,
                        textAlign = TextAlign.Center,
                        style = ClodyTheme.typography.body3Medium,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(ClodyTheme.colors.mainYellow),
                    ) {
                        Text(
                            text = "확인 후 앱 종료",
                            color = ClodyTheme.colors.gray02,
                            style = ClodyTheme.typography.body3SemiBold,
                        )
                    }
                }
            }
        }
    }
}

@ClodyPreview
@Composable
private fun PreviewInspectionDialog() {
    BasePreview {
        InspectionDialog(
            onDismiss = {},
        )
    }
}
