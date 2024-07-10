package com.sopt.clody.presentation.auth

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.clody.presentation.main.MainNavigator

@Composable
fun TermsOfServiceRoute(
    navigator: MainNavigator
) {
    TermsOfServiceScreen(
        onAgreeClick = { navigator.navigateBack() }
    )
}

@Composable
fun TermsOfServiceScreen(
    onAgreeClick: () -> Unit
) {
    Button(onClick = { onAgreeClick() }) {
        Text("Agree")
    }

}

@Preview(showBackground = true)
@Composable
fun TermsOfServiceScreenPreview() {
    TermsOfServiceScreen(onAgreeClick = {})
}
