package com.sopt.clody.presentation.ui.setting.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    titleMsg: String,
    descriptionMsg: String,
    confirmOpt: String,
    dismissOpt: String,
    confirmAction: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .background(color = ClodyTheme.colors.white, shape = RoundedCornerShape(size = 12.dp))
                .height(176.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = titleMsg,
                    style = ClodyTheme.typography.body1SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = descriptionMsg,
                    color = ClodyTheme.colors.gray04,
                    textAlign = TextAlign.Center,
                    style = ClodyTheme.typography.body3Regular
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        onClick = { onDismiss() },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color = ClodyTheme.colors.gray07,
                                shape = RoundedCornerShape(size = 8.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(ClodyTheme.colors.gray07)
                    ) {
                        Text(text = dismissOpt,
                            color = ClodyTheme.colors.gray04,
                            style = ClodyTheme.typography.body3SemiBold)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { confirmAction() },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .background(
                                color = ClodyTheme.colors.darkYellow,
                                shape = RoundedCornerShape(size = 8.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(ClodyTheme.colors.darkYellow)
                    ) {
                        Text(text = confirmOpt,
                            color = ClodyTheme.colors.gray01,
                            style = ClodyTheme.typography.body3SemiBold)
                    }
                }
            }
        }
    }
}
