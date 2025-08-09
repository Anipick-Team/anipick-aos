package com.jparkbro.anipick.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jparkbro.anipick.R
import com.jparkbro.explore.navigation.Explore
import com.jparkbro.home.navigation.Home
import com.jparkbro.mypage.navigation.MyPage
import com.jparkbro.ranking.navigation.Ranking
import kotlin.reflect.KClass

enum class BottomDestination(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val labelTextId: Int,
    val route: KClass<*>,
) {
    HOME(
        selectedIcon = R.drawable.ic_home_on,
        unselectedIcon = R.drawable.ic_home_off,
        labelTextId = R.string.home,
        route = Home::class,
    ),
    RANKING(
        selectedIcon = R.drawable.ic_ranking_on,
        unselectedIcon = R.drawable.ic_ranking_off,
        labelTextId = R.string.ranking,
        route = Ranking::class,
    ),
    EXPLORE(
        selectedIcon = R.drawable.ic_search_on,
        unselectedIcon = R.drawable.ic_search_off,
        labelTextId = R.string.explore,
        route = Explore::class,
    ),
    MY_PAGE(
        selectedIcon = R.drawable.ic_profile_on,
        unselectedIcon = R.drawable.ic_profile_off,
        labelTextId = R.string.profile,
        route = MyPage::class,
    ),
}