package com.jparkbro.anipick.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.jparkbro.detail.navigation.detailAnimeScreen
import com.jparkbro.detail.navigation.navigateToAnimeDetail
import com.jparkbro.detail.navigation.navigateToStudioDetail
import com.jparkbro.detail.navigation.studioDetailScreen
import com.jparkbro.email.navigation.emailLoginScreen
import com.jparkbro.email.navigation.emailSignupScreen
import com.jparkbro.email.navigation.navigateToEmailLogin
import com.jparkbro.email.navigation.navigateToEmailSignup
import com.jparkbro.explore.navigation.exploreScreen
import com.jparkbro.explore.navigation.navigateToExplore
import com.jparkbro.findpassword.navigation.navigateToPasswordReset
import com.jparkbro.findpassword.navigation.navigateToPasswordVerification
import com.jparkbro.findpassword.navigation.passwordResetScreen
import com.jparkbro.findpassword.navigation.passwordVerificationScreen
import com.jparkbro.home.navigation.homeDetailScreen
import com.jparkbro.home.navigation.homeScreen
import com.jparkbro.home.navigation.navigateToHome
import com.jparkbro.home.navigation.navigateToHomeDetail
import com.jparkbro.login.navigation.loginScreen
import com.jparkbro.login.navigation.navigateToLogin
import com.jparkbro.model.common.MetaData
import com.jparkbro.mypage.navigation.myPageScreen
import com.jparkbro.preferencesetup.navigation.navigateToPreferenceSetup
import com.jparkbro.preferencesetup.navigation.preferenceSetupScreen
import com.jparkbro.mypage.navigation.myRatingsScreen
import com.jparkbro.mypage.navigation.navigateToMyRatings
import com.jparkbro.mypage.navigation.navigateToUserContent
import com.jparkbro.mypage.navigation.userContentScreen
import com.jparkbro.ranking.navigation.navigateToRanking
import com.jparkbro.ranking.navigation.rankingScreen
import com.jparkbro.review.navigation.navigateToReviewForm
import com.jparkbro.review.navigation.reviewFormScreen
import com.jparkbro.search.navigation.navigateToSearch
import com.jparkbro.search.navigation.navigateToSearchResult
import com.jparkbro.search.navigation.searchResultScreen
import com.jparkbro.search.navigation.searchScreen
import com.jparkbro.setting.navigation.navigateToProfileEdit
import com.jparkbro.setting.navigation.navigateToSetting
import com.jparkbro.setting.navigation.profileEditScreen
import com.jparkbro.setting.navigation.settingScreen

@Composable
fun APNavHost(
    navController: NavHostController,
    startDestination: Any,
    metaData: MetaData,
    bottomNav: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginScreen(
            onNavigateToHome = navController::navigateToHome,
            onNavigateToPreferenceSetup = navController::navigateToPreferenceSetup,
            onNavigateToEmailLogin = navController::navigateToEmailLogin,
            onNavigateToEmailSignup = navController::navigateToEmailSignup,
        )
        emailLoginScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToEmailSignup = navController::navigateToEmailSignup,
            onNavigateToFindPassword = navController::navigateToPasswordVerification,
            onNavigateToHome = navController::navigateToHome,
            onNavigateToPreferenceSetup = navController::navigateToPreferenceSetup
        )
        emailSignupScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToPreferenceSetup = navController::navigateToPreferenceSetup,
        )
        passwordVerificationScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToPasswordReset = navController::navigateToPasswordReset
        )
        passwordResetScreen(
            onNavigateBack = navController::navigateUp,
        )
        preferenceSetupScreen(
            metaData = metaData,
            onNavigateToHome = navController::navigateToHome,
        )

        homeScreen(
            bottomNav = bottomNav,
            onNavigateToSearch = navController::navigateToSearch,
            onNavigateToAnimeDetail = navController::navigateToAnimeDetail,
            onNavigateToRanking = { navController.navigateToRanking() },
            onNavigateToExplore = { year, quarter ->
                navController.navigateToExplore(year = year, quarter = quarter)
            },
            onNavigateToHomeDetail = navController::navigateToHomeDetail,
        )
        homeDetailScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToAnimeDetail = navController::navigateToAnimeDetail,
            onNavigateToReviewForm = navController::navigateToReviewForm,
        )
        rankingScreen(
            metaData = metaData,
            bottomNav = bottomNav,
            onNavigateToSearch = navController::navigateToSearch,
            onNavigateToAnimeDetail = navController::navigateToAnimeDetail,
        )
        exploreScreen(
            metaData = metaData,
            bottomNav = bottomNav,
            onNavigateToSearch = navController::navigateToSearch,
            onNavigateToAnimeDetail = navController::navigateToAnimeDetail
        )

        /* MyPage */
        myPageScreen(
            bottomNav = bottomNav,
            onNavigateToAnimeDetail = navController::navigateToAnimeDetail,
            onNavigateToUserContent = navController::navigateToUserContent,
            onNavigateToMyRatings = navController::navigateToMyRatings,
            onNavigateToSetting = navController::navigateToSetting,
            onCheckSettingRefresh = { navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("setting_refresh") ?: false },
            onClearSettingRefresh = { navController.previousBackStackEntry?.savedStateHandle?.set("setting_refresh", false) },
            onCheckStatusRefresh = { navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("status_refresh") ?: false },
            onClearStatusRefresh = { navController.previousBackStackEntry?.savedStateHandle?.set("status_refresh", false) },
        )
        userContentScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToAnimeDetail = navController::navigateToAnimeDetail,
            onCheckStatusRefresh = { navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("status_refresh") ?: false },
            onStatusRefresh = { navController.previousBackStackEntry?.savedStateHandle?.set("status_refresh", true) }
        )
        myRatingsScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToReviewForm = navController::navigateToReviewForm,
            onNavigateToAnimeDetail = navController::navigateToAnimeDetail,
        )

        detailAnimeScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToReviewForm = navController::navigateToReviewForm,
            onNavigateToStudioDetail = navController::navigateToStudioDetail,
            onCheckReviewRefresh = { navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("review_refresh") ?: false },
            onClearReviewRefresh = { navController.previousBackStackEntry?.savedStateHandle?.set("review_refresh", false) },
            onStatusRefresh = { navController.previousBackStackEntry?.savedStateHandle?.set("status_refresh", true) }
        )
        studioDetailScreen(
            onNavigateBack = navController::navigateUp
        )

        /* Setting */
        settingScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToProfileEdit = navController::navigateToProfileEdit,
            onNavigateToLogin = {
                navController.navigateToLogin(navOptions {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                })
            },
            onCheckSettingRefresh = { navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("setting_refresh") ?: false },
            onPopBackWithRefresh = {
                navController.previousBackStackEntry?.savedStateHandle?.set("setting_refresh", true)
            }
        )
        profileEditScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToLogin = {
                navController.navigateToLogin(navOptions {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                })
            },
            onPopBackWithRefresh = {
                navController.previousBackStackEntry?.savedStateHandle?.set("setting_refresh", true)
                navController.popBackStack()
            }
        )

        /* Search */
        searchScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToAnimeDetail = navController::navigateToAnimeDetail,
            onNavigateSearchResult = navController::navigateToSearchResult
        )
        searchResultScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToAnimeDetail = navController::navigateToAnimeDetail
        )

        /* review */
        reviewFormScreen(
            onNavigateBack = navController::navigateUp,
            onPopBackWithRefresh = {
                navController.previousBackStackEntry?.savedStateHandle?.set("review_refresh", true)
                navController.popBackStack()
            }
        )
    }
}

fun NavOptionsBuilder.clearBackStack(navController: NavHostController) {
    popUpTo(navController.graph.id) {
        saveState = false
//        saveState = true
    }
    launchSingleTop = true
//    restoreState = true
    restoreState = false
}