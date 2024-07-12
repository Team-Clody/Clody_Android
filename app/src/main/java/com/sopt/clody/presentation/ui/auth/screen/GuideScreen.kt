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

@Composable
fun GuideRoute(
    navigator: AuthNavigator
) {
    GuideScreen(onNextButtonClick = { navigator.navigateTimeReminder() }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GuideScreen(
    onNextButtonClick: () -> Unit
) {
    val pages = listOf(
        BoardingPage("안녕하세요!", "저는 로디라고 해요", "여러분이 써준 감사일기를 받고,\n칭찬과 응원을 담아 답장을 쓴답니다", R.drawable.img_guide_first),
        BoardingPage("답장마다 행운의", "네잎클로버를 함께 드려요", "하루에 받은 감사의 수가 많을수록\n" + "색이 진한 네잎클로버를 전달해요", R.drawable.img_guide_second),
        BoardingPage("오늘의 일기만", "작성할 수 있어요", "전날이나 다음날의 일기는 작성할 수\n" + "없으니, 잊지 말고 기록해주세요", R.drawable.img_guide_third),
        BoardingPage("이제 일기를 써볼까요?", "기다리고 있을게요!", "두번째 일기부터는 네잎클로버를 찾는 데\n" + "12시간이 걸리니 조금만 기다려 주세요", R.drawable.img_guide_fourth)
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
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
