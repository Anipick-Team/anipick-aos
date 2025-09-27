package com.jparkbro.mypage.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jparkbro.model.common.FormType
import com.jparkbro.model.mypage.ContentType
import com.jparkbro.mypage.MyPage
import com.jparkbro.mypage.MyRatings
import com.jparkbro.mypage.UserContent
import com.jparkbro.mypage.UserContentViewModel
import kotlinx.serialization.Serializable

@Serializable data object MyPage

@Serializable data class UserContent(val type: ContentType)

@Serializable data object MyRatings

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) = navigate(MyPage, navOptions)

fun NavGraphBuilder.myPageScreen(
    bottomNav: @Composable () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToUserContent: (ContentType) -> Unit,
    onNavigateToMyRatings: () -> Unit,
    onNavigateToSetting: () -> Unit,
    onNavigateToActorDetail: (Int) -> Unit,
    onCheckSettingRefresh: () -> Boolean,
    onClearSettingRefresh: () -> Unit,
    onCheckStatusRefresh: () -> Boolean,
    onClearStatusRefresh: () -> Unit,
) {
    composable<MyPage> { entry ->
        MyPage(
            bottomNav = bottomNav,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            onNavigateToUserContent = onNavigateToUserContent,
            onNavigateToMyRatings = onNavigateToMyRatings,
            onNavigateToSetting = onNavigateToSetting,
            onNavigateToActorDetail = onNavigateToActorDetail,
            onCheckSettingRefresh = onCheckSettingRefresh,
            onClearSettingRefresh = onClearSettingRefresh,
            onCheckStatusRefresh = onCheckStatusRefresh,
            onClearStatusRefresh = onClearStatusRefresh,
        )
    }
}

fun NavController.navigateToUserContent(type: ContentType, navOptions: NavOptions? = null) = navigate(UserContent(type), navOptions)

fun NavGraphBuilder.userContentScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
    onNavigateToActorDetail: (Int) -> Unit,
    onCheckStatusRefresh: () -> Boolean,
    onStatusRefresh: () -> Unit,
) {
    composable<UserContent> { entry ->
        val route = entry.toRoute<UserContent>()

        UserContent(
            onNavigateBack = onNavigateBack,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
            onNavigateToActorDetail = onNavigateToActorDetail,
            onCheckStatusRefresh = onCheckStatusRefresh,
            onStatusRefresh = onStatusRefresh,
            viewModel = hiltViewModel<UserContentViewModel, UserContentViewModel.Factory>(
                key = route.type.title
            ) { factory ->
                factory.create(route.type)
            }
        )
    }
}

fun NavController.navigateToMyRatings(navOptions: NavOptions? = null) = navigate(MyRatings, navOptions)

fun NavGraphBuilder.myRatingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToReviewForm: (Int, Int, FormType) -> Unit,
    onNavigateToAnimeDetail: (Int) -> Unit,
) {
    composable<MyRatings> {
        MyRatings(
            onNavigateBack = onNavigateBack,
            onNavigateToReviewForm = onNavigateToReviewForm,
            onNavigateToAnimeDetail = onNavigateToAnimeDetail,
        )
    }
}