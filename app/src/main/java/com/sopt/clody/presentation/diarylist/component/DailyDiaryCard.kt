package com.sopt.clody.presentation.diarylist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DailyDiaryCard(index: Int, order: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 14.dp)
            .wrapContentSize(align = Alignment.TopStart),
        colors = CardDefaults.cardColors(containerColor = ClodyTheme.colors.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .padding(bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .height(28.dp)
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Image(
                    painter = painterResource(id = R.drawable.ic_listview_clover),
                    contentDescription = "clover",
                    modifier = Modifier
                        .padding(end = 6.dp)
                )
                Text(
                    text = "26일",
                    modifier = Modifier
                        .padding(end = 2.dp),
                    style = ClodyTheme.typography.body1SemiBold
                )
                Text(
                    text = "/목요일",
                    style = ClodyTheme.typography.body2SemiBold,
                    color = ClodyTheme.colors.gray04
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = ClodyTheme.colors.lightBlue),
                    shape = RoundedCornerShape(size = 9.dp),
                    onClick = { /* 답장 확인 */ },
                    contentPadding = PaddingValues(0.dp) // button에 설정된 기본 패딩을 제거
                ) {
                    Text(
                        text = "답장 확인",
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 2.dp),
                        style = ClodyTheme.typography.detail1SemiBold,
                        color = ClodyTheme.colors.blue
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_listview_kebab_menu),
                    contentDescription = "케밥 메뉴",
                    modifier = Modifier
                        .clickable(onClick = { /* 삭제하기 바텀 모달 시트 노출 */})
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            DailyDiaryCardItem(index)
        }
    }
}
