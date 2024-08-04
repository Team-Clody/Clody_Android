package com.sopt.clody.presentation.ui.auth.screen

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun GuideRoute(
    navigator: AuthNavigator
) {
    GuideScreen(onNextButtonClick = { navigator.navigateHome(LocalDate.now().year, LocalDate.now().monthValue) }
    )
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

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ClodyTheme.colors.white)
            .padding(24.dp)
    ) {
        val (pager, indicator, nextButton) = createRefs()
        val topGuideline = createGuidelineFromTop(0.161f)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.constrainAs(pager) {
                top.linkTo(topGuideline)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
            }
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = pages[page].description,
                    style = ClodyTheme.typography.body1Medium,
                    color = ClodyTheme.colors.gray05,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(66.dp))
                Image(
                    painter = painterResource(id = pages[page].imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Row(
            modifier = Modifier
                .constrainAs(indicator) {
                    bottom.linkTo(nextButton.top, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
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

        val coroutineScope = rememberCoroutineScope()
        ClodyButton(
            onClick = {
                coroutineScope.launch {
                    if (pagerState.currentPage < pages.size - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        onNextButtonClick()
                    }
                }
            },
            text = if (pagerState.currentPage < pages.size - 1) "다음" else "시작하기",
            enabled = true,
            modifier = Modifier.constrainAs(nextButton) {
                bottom.linkTo(parent.bottom, margin = 46.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
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
