package com.sopt.clody.presentation.auth

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.clody.presentation.main.MainNavigator


@Composable
fun RegisterRoute(
    navigator: MainNavigator
) {
    RegisterScreen(
        onTermsOfServiceClick = { navigator.navigateTermsOfService() }
    )
}

@Composable
fun RegisterScreen(
    onTermsOfServiceClick: () -> Unit
) {
    Button(onClick = { onTermsOfServiceClick() }) {
        Text("Terms of Service")
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onTermsOfServiceClick = {})
}
