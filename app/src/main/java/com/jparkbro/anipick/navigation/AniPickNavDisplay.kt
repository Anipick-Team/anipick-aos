package com.jparkbro.anipick.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import com.jparkbro.anipick.deeplink.DeepLinkViewModel
import com.jparkbro.anipick.deeplink.parseDeepLink
import com.jparkbro.auth.api.navigateToLogin
import com.jparkbro.auth.impl.navigation.authEntry
import com.jparkbro.catalog.impl.navigation.catalogEntry
import com.jparkbro.community.impl.navigation.communityEntry
import com.jparkbro.core.navigation.NavigationState
import com.jparkbro.core.navigation.Navigator
import com.jparkbro.core.navigation.toEntries
import com.jparkbro.explore.impl.navigation.exploreEntry
import com.jparkbro.home.api.navigateToHomeMain
import com.jparkbro.home.impl.navigation.homeEntry
import com.jparkbro.mypage.impl.navigation.myPageEntry
import com.jparkbro.ranking.impl.navigation.rankingEntry
import com.jparkbro.review.impl.navigation.reviewEntry
import com.jparkbro.search.impl.navigation.searchEntry
import com.jparkbro.splash.impl.navigation.splashEntry

private const val SLIDE_DURATION_MILLIS = 300

/** 바텀 네비게이션 탭의 루트 화면들 — 이 화면들끼리 오갈 때는 애니메이션 없이 바로 바꾼다. */
private val TOP_LEVEL_CONTENT_KEYS = BOTTOM_NAV_ITEMS.values.map { it.contentKey }.toSet()

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun APNavDisplay(
    navigationState: NavigationState,
    navigator: Navigator,
    bottomNavigation: @Composable () -> Unit,
    deepLinkViewModel: DeepLinkViewModel,
    modifier: Modifier = Modifier
) {
    SharedTransitionLayout(modifier = modifier) {
        val entryProvider = entryProvider<NavKey> {
            splashEntry(
                navigator = navigator,
                sharedTransitionScope = this@SharedTransitionLayout,
                onNavigateToHome = {
                    // 홈으로 먼저 진입(top-level 전환)한 다음에 딥링크 타겟을 push해야
                    // Splash의 서브스택이 아니라 Home 서브스택에 쌓인다 - 뒤로가기 시 홈으로 돌아옴.
                    navigator.navigateToHomeMain()
                    deepLinkViewModel.state.value.pendingUri
                        ?.let(::parseDeepLink)
                        ?.let(navigator::navigate)
                    deepLinkViewModel.consumeDeepLink()
                },
            )
            authEntry(navigator, sharedTransitionScope = this@SharedTransitionLayout)
            homeEntry(navigator, bottomNavigation)
            rankingEntry(navigator, bottomNavigation)
            exploreEntry(navigator, bottomNavigation)
            myPageEntry(
                navigator,
                bottomNavigation,
                onWithdrawSuccess = navigator::navigateToLogin,
                onLogout = navigator::navigateToLogin,
            )
            searchEntry(navigator)
            catalogEntry(navigator)
            communityEntry(navigator)
            reviewEntry(navigator)
        }

        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = navigator::goBack,
            sharedTransitionScope = this@SharedTransitionLayout,
            transitionSpec = { if (isTopLevelSwitch()) noTransition() else slideTransition(SlideDirection.Left) },
            popTransitionSpec = { if (isTopLevelSwitch()) noTransition() else slideTransition(SlideDirection.Right) },
            predictivePopTransitionSpec = { if (isTopLevelSwitch()) noTransition() else slideTransition(SlideDirection.Right) },
        )
    }
}

/** 바텀 탭 루트 화면(Home/Ranking/Explore/MyPage)끼리 오가는 전환이면 true. 양쪽 다 탭 루트여야 한다. */
private fun AnimatedContentTransitionScope<Scene<NavKey>>.isTopLevelSwitch(): Boolean {
    val fromKey = initialState.entries.lastOrNull()?.contentKey
    val toKey = targetState.entries.lastOrNull()?.contentKey
    return fromKey in TOP_LEVEL_CONTENT_KEYS && toKey in TOP_LEVEL_CONTENT_KEYS
}

private fun noTransition(): ContentTransform =
    EnterTransition.None togetherWith ExitTransition.None

/**
 * 탭 루트끼리를 뺀 나머지 화면 전환은 좌우 슬라이드로 통일한다. 들어갈 때는 [SlideDirection.Left], 나갈 때는
 * 앱 안 뒤로가기와 시스템 뒤로가기(predictive back) 모두 [SlideDirection.Right].
 */
private fun AnimatedContentTransitionScope<Scene<NavKey>>.slideTransition(
    direction: SlideDirection,
): ContentTransform =
    slideIntoContainer(direction, tween(SLIDE_DURATION_MILLIS)) togetherWith
        slideOutOfContainer(direction, tween(SLIDE_DURATION_MILLIS))
