package com.jparkbro.mypage.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.model.enum.UserContentType
import com.jparkbro.model.mypage.ContentType
import com.jparkbro.mypage.main.MyPageRoot
import kotlinx.serialization.Serializable

@Serializable
data object MyPage

fun NavHostController.navigateToMyPage(
    navOptions: NavOptions? = null
) = navigate(MyPage, navOptions)

fun NavGraphBuilder.myPageScreen(
    bottomNav: @Composable () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToUserContent: (UserContentType) -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToActor: (Long) -> Unit,
) {
    composable<MyPage> {
        MyPageRoot(
            bottomNav = bottomNav,
            onNavigateToUserContent = onNavigateToUserContent,
            onNavigateToSetting = onNavigateToSetting,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
            onNavigateToActor = onNavigateToActor
        )
    }
}