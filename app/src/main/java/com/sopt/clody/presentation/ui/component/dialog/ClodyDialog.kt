package com.sopt.clody.presentation.ui.component.dialog

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun ClodyDialog(
    titleMassage: String,
    descriptionMassage: String,
    confirmOption: String,
    dismissOption: String,
    confirmAction: () -> Unit,
    confirmButtonColor: Color,
    confirmButtonTextColor: Color,
    onDismiss: () -> Unit,
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
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = titleMassage,
                        style = ClodyTheme.typography.body1SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = descriptionMassage,
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
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = ClodyTheme.colors.gray07,
                                    shape = RoundedCornerShape(size = 8.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(ClodyTheme.colors.gray07)
                        ) {
                            Text(
                                text = dismissOption,
                                color = ClodyTheme.colors.gray04,
                                style = ClodyTheme.typography.body3SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = confirmAction,
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = confirmButtonColor,
                                    shape = RoundedCornerShape(size = 8.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(confirmButtonColor)
                        ) {
                            Text(
                                text = confirmOption,
                                color = confirmButtonTextColor,
                                style = ClodyTheme.typography.body3SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

