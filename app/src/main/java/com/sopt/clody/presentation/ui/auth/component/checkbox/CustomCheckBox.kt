package com.sopt.clody.presentation.ui.auth.component.checkbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sopt.clody.R

@Composable
fun CustomCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    size: Dp,
    checkedImageRes: Int,
    uncheckedImageRes: Int
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable(
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Image(
                painter = painterResource(id = checkedImageRes),
                contentDescription = "",
            )
        } else {
            Image(
                painter = painterResource(id = uncheckedImageRes),
                contentDescription = "",
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCheckboxPreview() {
    CustomCheckbox(checked = true, onCheckedChange = {}, size = 23.dp, checkedImageRes = R.drawable.ic_terms_check_on_23, uncheckedImageRes = R.drawable.ic_terms_check_off_23)
}
