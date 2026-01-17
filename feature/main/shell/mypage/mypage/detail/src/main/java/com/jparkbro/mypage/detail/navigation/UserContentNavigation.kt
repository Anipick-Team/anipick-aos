package com.jparkbro.mypage.detail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.model.common.FormType
import com.jparkbro.model.enum.UserContentType
import com.jparkbro.mypage.detail.UserContentRoot
import kotlinx.serialization.Serializable

@Serializable data class UserContent(val contentType: UserContentType)

fun NavHostController.navigateToUserContent(
    contentType: UserContentType,
    navOptions: NavOptions? = null
) = navigate(UserContent(contentType), navOptions)

fun NavGraphBuilder.userContentScreen(
    onNavigateBack: () -> Unit,
    onNavigateToInfoAnime: (Long) -> Unit,
    onNavigateToActor: (Long) -> Unit,
    onNavigateToReviewForm: (Long, FormType) -> Unit,
) {
    composable<UserContent> {
        UserContentRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToInfoAnime = onNavigateToInfoAnime,
            onNavigateToActor = onNavigateToActor,
            onNavigateToReviewForm = onNavigateToReviewForm,
        )
    }
}