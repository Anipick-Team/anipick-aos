package jpark.bro.anipick.ui.navigation

import androidx.annotation.DrawableRes
import jpark.bro.anipick.R

object AniPickDestinations {
    object Auth {
        const val Login = "login"
        const val EmailLogin = "email-login"
        const val EmailSignup = "email-signup"
        const val FindPassword = "find-password"
    }

    const val MainScreen = "main-screen"

    object Tabs {
        const val Home = "home"
        const val Ranking = "ranking"
        const val Explore = "explore"
        const val Profile = "profile"

        val items = listOf(
            BottomNavItem(Home, "홈", R.drawable.ic_home_fill, R.drawable.ic_home_outline),
            BottomNavItem(Ranking, "랭킹", R.drawable.ic_rank_fill, R.drawable.ic_rank_outline),
            BottomNavItem(Explore, "탐색", R.drawable.ic_search_fill, R.drawable.ic_search_outline),
            BottomNavItem(Profile, "프로필", R.drawable.ic_profile_fill, R.drawable.ic_profile_outline),
        )
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
)

/*
// 네비게이션 관련 확장 함수들
object APNavigationActions {
    // 로그인 성공 후 메인 화면으로 이동
    fun NavHostController.navigateToMainScreen() {
        this.navigate(APDestinations.MainScreen) {
            // 로그인 스택은 제거 (뒤로가기 시 앱 종료)
            popUpTo(APDestinations.Auth.Login) { inclusive = true }
        }
    }

    // 로그아웃 시 로그인 화면으로 이동
    fun NavHostController.navigateToLogin() {
        this.navigate(APDestinations.Auth.Login) {
            // 메인 스크린 스택 제거
            popUpTo(APDestinations.MainScreen) { inclusive = true }
        }
    }
}
*/