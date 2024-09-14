package com.sopt.clody.presentation.ui.auth.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GuideRoute(
    navigator: AuthNavigator
) {
    GuideScreen(onNextButtonClick = { navigator.navigateHome() })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuideScreen(
    onNextButtonClick: () -> Unit
) {
    val pages = listOf(
        BoardingPage(
            title = stringResource(R.string.guide_page1_title),
            subtitle = stringResource(R.string.guide_page1_subtitle),
            description = stringResource(R.string.guide_page1_description),
            imageRes = R.drawable.img_guide_first
        ),
        BoardingPage(
            title = stringResource(R.string.guide_page2_title),
            subtitle = stringResource(R.string.guide_page2_subtitle),
            description = stringResource(R.string.guide_page2_description),
            imageRes = R.drawable.img_guide_second
        ),
        BoardingPage(
            title = stringResource(R.string.guide_page3_title),
            subtitle = stringResource(R.string.guide_page3_subtitle),
            description = stringResource(R.string.guide_page3_description),
            imageRes = R.drawable.img_guide_third
        ),
        BoardingPage(
            title = stringResource(R.string.guide_page4_title),
            subtitle = stringResource(R.string.guide_page4_subtitle),
            description = stringResource(R.string.guide_page4_description),
            imageRes = R.drawable.img_guide_fourth
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()
    var isExiting by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            ClodyButton(
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < pages.size - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            isExiting = true
                        }
                    }
                },
                text = if (pagerState.currentPage < pages.size - 1) {
                    stringResource(id = R.string.guide_next)
                } else {
                    stringResource(id = R.string.guide_start)
                },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp)
            )
        },
        content = { innerPadding ->
            AnimatedVisibility(
                visible = !isExiting,
                exit = fadeOut(animationSpec = tween(1000)), // 1초 페이드 아웃
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = ClodyTheme.colors.white),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.heightForScreenPercentage(0.21f))
                    HorizontalPager(
                        state = pagerState,
                    ) { page ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = pages[page].title,
                                style = ClodyTheme.typography.head1,
                                color = ClodyTheme.colors.gray01,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = pages[page].subtitle,
                                style = ClodyTheme.typography.head1,
                                color = ClodyTheme.colors.gray01,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.heightForScreenPercentage(0.02f))
                            Text(
                                text = pages[page].description,
                                style = ClodyTheme.typography.body1Medium,
                                color = ClodyTheme.colors.gray05,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.heightForScreenPercentage(0.04f))
                            Image(
                                painter = painterResource(id = pages[page].imageRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    Spacer(modifier = Modifier.heightForScreenPercentage(0.2f))
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color = if (pagerState.currentPage == iteration) ClodyTheme.colors.gray03 else ClodyTheme.colors.gray07
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(6.dp)
                            )
                        }
                    }
                }
            }

            if (isExiting) {
                LaunchedEffect(Unit) {
                    delay(1000)
                    onNextButtonClick()
                }
            }
        }
    )
}

data class BoardingPage(
    val title: String,
    val subtitle: String,
    val description: String,
    val imageRes: Int
)

@Preview(showBackground = true)
@Composable
fun PreviewBoardingScreen() {
    GuideScreen(
        onNextButtonClick = { }
    )
}
