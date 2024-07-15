package com.sopt.clody.presentation.ui.replydiary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyDiaryScreen() {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        CloverDialog(
            onDismiss = { showDialog = false },
            titleMassage = "길동님을 위한 행운 도착",
            descriptionMassage = "1개의 네잎클로버 획득",
            confirmOption = "확인",
            confirmAction = {
                showDialog = false
            },
            confirmButtonColor = ClodyTheme.colors.mainYellow
        )
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "5월 10일 금요일",
                            style = ClodyTheme.typography.head4,
                            color = ClodyTheme.colors.gray01,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Image(
                                painterResource(id = R.drawable.ic_nickname_back),
                                contentDescription = "back"
                            )
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(ClodyTheme.colors.white)
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp)
                ) {
                    Spacer(modifier = Modifier.height(13.dp))

                    Box(
                        contentAlignment = Alignment.TopCenter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 28.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(ClodyTheme.colors.gray08)
                            .weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_reply_logo),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(top = 20.dp)
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "문수님을 위한 행운의 답장",
                                    style = ClodyTheme.typography.body2Medium,
                                    color = ClodyTheme.colors.gray01,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    text = "너무 잘 그렸다고 칭찬해 주셨어요. 정말 기뻤답니다. 점심 시간에는 친구들과 함께 점심을 먹었어요. 오늘의 점심 메뉴는 김밥이었어요. 저는 김밥을 아주 좋아해서 신나게 먹었어요. 점심을 다 먹고 나서는 운동장에서 친구들과 술래잡기를 했어요. 오늘은 제가 술래가 되어서 친구들을 잡으려고 열심히 뛰어 다녔어요. 모두가 웃고 떠들며 즐거운 시간을 보냈어요. 점심 시간이 끝나고 나서는 체육 시간이었어요. 오늘은 축구를 했어요. 저는 골키퍼 역할을 맡았는데, 친구들이 공을 차올 때마다 열심히 막아냈어요. 한 번은 공이 너무 세게 날아와서 조금 겁이 났지만, 그래도 용기를 내서 막았답니다. 아아아아아아낭미아아아아아아아아아아아아아아아아아아아아아아아아아낭미아아아아아아\n" +
                                            "너무 잘 그렸다고 칭찬해 주셨어. 칭찬해 주셨어\n",
                                    modifier = Modifier.padding(24.dp),
                                    style = ClodyTheme.typography.letterMedium,
                                    color = ClodyTheme.colors.gray02,
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewReplyScreen() {
    ReplyDiaryScreen()
}
