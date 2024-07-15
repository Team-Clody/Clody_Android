package com.sopt.clody.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavHostController) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        scope.launch {
            delay(1000)
            navController.navigate("register_graph") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ClodyTheme.colors.mainYellow),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_splash_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(128.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {

}
