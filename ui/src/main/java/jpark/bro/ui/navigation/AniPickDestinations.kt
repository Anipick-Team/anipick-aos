package jpark.bro.ui.navigation

import androidx.annotation.DrawableRes
import jpark.bro.ui.R

sealed class AniPickDestinations(val route: String) {

    sealed class Auth(route: String) : AniPickDestinations(route) {
        object Login: Auth("login")
        object EmailLogin : Auth("email-login")
        object EmailSignup : Auth("email-signup")
        object PasswordVerification : Auth("password-verification")
        object PasswordReset : Auth("password-reset")
    }

    object MainScreen : AniPickDestinations("main-screen")
    object Search : AniPickDestinations("search")

    sealed class Tabs(route: String, val label: String, @DrawableRes val selectedIcon: Int, @DrawableRes val unselectedIcon: Int) : AniPickDestinations(route) {
        object Home : Tabs("home", "홈", R.drawable.ic_home_fill, R.drawable.ic_home_outline)
        object Ranking : Tabs("ranking", "랭킹", R.drawable.ic_rank_fill, R.drawable.ic_rank_outline)
        object Explore : Tabs("explore", "탐색", R.drawable.ic_search_fill, R.drawable.ic_search_outline)
        object Profile : Tabs("profile", "프로필", R.drawable.ic_profile_fill, R.drawable.ic_profile_outline)

        companion object {
            val items = listOf(Home, Ranking, Explore, Profile)
        }
    }

    /*// 기본 경로 가져오기 (인수 없는 형태)
    fun createRoute() = route

    // 인수가 있는 경로는 이런 방식으로 확장 가능
    sealed class Detail(route: String) : AniPickDestinations(route) {
        object ItemDetail : Detail("item-detail/{itemId}") {
            const val ARG_ITEM_ID = "itemId"

            // 인수가 있는 경로 생성 함수
            fun createRoute(itemId: String) = "item-detail/$itemId"
        }
    }*/
}