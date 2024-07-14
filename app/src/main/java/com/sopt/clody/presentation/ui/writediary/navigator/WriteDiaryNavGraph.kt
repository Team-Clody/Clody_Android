package com.sopt.clody.presentation.ui.writediary.navigator

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.auth.screen.GuideRoute
import com.sopt.clody.presentation.ui.auth.screen.NicknameRoute
import com.sopt.clody.presentation.ui.auth.screen.SignUpRoute
import com.sopt.clody.presentation.ui.auth.screen.TermsOfServiceRoute
import com.sopt.clody.presentation.ui.auth.screen.TimeReminderRoute
import com.sopt.clody.presentation.ui.writediary.screen.WriteDiaryRoute


fun NavGraphBuilder.writeDiaryNavGraph(
    navigator: WriteDiaryNavigator
) {
    composable("write_diary") {
        WriteDiaryRoute()
    }
}
