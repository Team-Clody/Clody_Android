package com.sopt.clody.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sopt.clody.presentation.ui.navigatior.MainNavHost
import com.sopt.clody.presentation.ui.navigatior.MainNavigator
import com.sopt.clody.ui.theme.CLODYTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CLODYTheme {
                val navController = rememberNavController()
                val navigator = remember(navController) { MainNavigator(navController) }

                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    content = { paddingValues ->
                        MainNavHost(
                            modifier = Modifier.padding(paddingValues),
                            navigator = navigator
                        )
                    }
                )
            }
        }
    }
}
