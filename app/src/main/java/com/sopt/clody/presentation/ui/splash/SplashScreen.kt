package com.sopt.clody.presentation.ui.splash

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.sopt.clody.R
import com.sopt.clody.domain.model.AppUpdateState
import com.sopt.clody.presentation.ui.component.dialog.InspectionDialog
import com.sopt.clody.presentation.utils.appupdate.AppUpdateUtils
import com.sopt.clody.presentation.utils.base.BasePreview
import com.sopt.clody.presentation.utils.base.ClodyPreview
import com.sopt.clody.presentation.utils.extension.repeatOnStarted
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun SplashRoute(
    viewModel: SplashViewModel = mavericksViewModel(),
    startIntent: Intent,
    onLoginRequired: () -> Unit,
    onAlreadyLoggedIn: () -> Unit,
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        viewModel.postIntent(SplashContract.SplashIntent.InitSplash(startIntent))

        lifecycleOwner.repeatOnStarted {
            viewModel.sideEffects.collect { effect ->
                when (effect) {
                    is SplashContract.SplashSideEffect.NavigateToLogin -> onLoginRequired()
                    is SplashContract.SplashSideEffect.NavigateToHome -> onAlreadyLoggedIn()
                    is SplashContract.SplashSideEffect.NavigateToMarketAndFinish -> {
                        AppUpdateUtils.navigateToMarketAndFinish(activity)
                    }

                    is SplashContract.SplashSideEffect.FinishApp -> {
                        activity.finishAffinity()
                    }

                    is SplashContract.SplashSideEffect.NavigateToMarket -> {
                        AppUpdateUtils.navigateToMarket(context)
                    }
                }
            }
        }
    }

    when (val updateState = state.updateState) {
        is AppUpdateState.SoftUpdate -> {
            SoftUpdateDialog(
                latestVersion = updateState.latestVersion,
                onDismiss = { viewModel.postIntent(SplashContract.SplashIntent.ClearUpdateState) },
                onConfirm = {
                    viewModel.postIntent(SplashContract.SplashIntent.HandleSoftUpdateConfirm)
                },
            )
        }

        is AppUpdateState.HardUpdate -> {
            HardUpdateDialog(
                latestVersion = updateState.latestVersion,
                onConfirm = {
                    viewModel.postIntent(SplashContract.SplashIntent.HandleHardUpdate(isConfirm = true))
                },
                onExit = {
                    viewModel.postIntent(SplashContract.SplashIntent.HandleHardUpdate(isConfirm = false))
                },
            )
        }

        else -> {}
    }

    if (state.showInspectionDialog) {
        InspectionDialog(
            inspectionTime = state.inspectionTimeText.orEmpty(),
            onDismiss = {
                viewModel.postIntent(SplashContract.SplashIntent.DismissInspectionDialog)
            },
        )
    }
    SplashScreen()
}

@Composable
fun SplashScreen() {
    val backgroundColor = ClodyTheme.colors.mainYellow
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_splash_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(160.dp),
        )
    }
}

@Composable
fun SoftUpdateDialog(
    latestVersion: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_soft_update_title)) },
        text = {
            Text(
                text = stringResource(R.string.dialog_soft_update_description, latestVersion),
                textAlign = TextAlign.Center,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.dialog_soft_update_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dialog_soft_update_dismiss))
            }
        },
    )
}

@Composable
fun HardUpdateDialog(
    latestVersion: String,
    onConfirm: () -> Unit,
    onExit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.dialog_hard_update_title)) },
        text = {
            Text(stringResource(R.string.dialog_hard_update_description, latestVersion))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.dialog_hard_update_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text(stringResource(R.string.dialog_hard_update_exit))
            }
        },
    )
}

@ClodyPreview
@Composable
fun SplashScreenPreview() {
    BasePreview {
        SplashScreen()
    }
}
