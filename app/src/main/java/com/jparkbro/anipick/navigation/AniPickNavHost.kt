package com.jparkbro.anipick.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.jparkbro.actor.navigation.actorScreen
import com.jparkbro.actor.navigation.navigateToActor
import com.jparkbro.explore.navigation.exploreScreen
import com.jparkbro.explore.navigation.navigateToExplore
import com.jparkbro.home.detail.navigation.homeDetailScreen
import com.jparkbro.home.detail.navigation.navigateToHomeDetail
import com.jparkbro.home.main.navigation.homeScreen
import com.jparkbro.home.main.navigation.navigateToHome
import com.jparkbro.info.anime.navigation.infoAnimeScreen
import com.jparkbro.info.anime.navigation.navigateToInfoAnime
import com.jparkbro.info.character.navigation.infoCharacterScreen
import com.jparkbro.info.character.navigation.navigateToInfoCharacter
import com.jparkbro.info.recommend.navigation.infoRecommendScreen
import com.jparkbro.info.recommend.navigation.navigateToInfoRecommend
import com.jparkbro.info.series.navigation.infoSeriesScreen
import com.jparkbro.info.series.navigation.navigateToInfoSeries
import com.jparkbro.login.navigation.emailLoginScreen
import com.jparkbro.login.navigation.loginScreen
import com.jparkbro.login.navigation.navigateToEmailLogin
import com.jparkbro.login.navigation.navigateToLogin
import com.jparkbro.model.common.MetaData
import com.jparkbro.mypage.detail.navigation.navigateToUserContent
import com.jparkbro.mypage.detail.navigation.userContentScreen
import com.jparkbro.mypage.main.navigation.myPageScreen
import com.jparkbro.preferencesetup.navigation.navigateToPreferenceSetup
import com.jparkbro.preferencesetup.navigation.preferenceSetupScreen
import com.jparkbro.ranking.navigation.navigateToRanking
import com.jparkbro.ranking.navigation.rankingScreen
import com.jparkbro.register.navigation.emailRegisterScreen
import com.jparkbro.register.navigation.navigateToEmailRegister
import com.jparkbro.reset.navigation.navigateToPasswordReset
import com.jparkbro.reset.navigation.passwordResetScreen
import com.jparkbro.review.navigation.navigateToReviewForm
import com.jparkbro.review.navigation.reviewFormScreen
import com.jparkbro.search.detail.navigation.navigateToSearchResult
import com.jparkbro.search.detail.navigation.searchResultScreen
import com.jparkbro.search.main.navigation.navigateToSearch
import com.jparkbro.search.main.navigation.searchScreen
import com.jparkbro.setting.detail.navigation.navigateToUserEdit
import com.jparkbro.setting.detail.navigation.userEditScreen
import com.jparkbro.setting.main.navigation.navigateToSetting
import com.jparkbro.setting.main.navigation.settingScreen
import com.jparkbro.studio.navigation.navigateToStudio
import com.jparkbro.studio.navigation.studioScreen
import com.jparkbro.verification.navigation.navigateToPasswordVerification
import com.jparkbro.verification.navigation.passwordVerificationScreen

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
            onNavigateToEmailRegister = navController::navigateToEmailRegister,
        )
        emailLoginScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToEmailRegister = navController::navigateToEmailRegister,
            onNavigateToFindPassword = navController::navigateToPasswordVerification,
            onNavigateToHome = navController::navigateToHome,
            onNavigateToPreferenceSetup = navController::navigateToPreferenceSetup
        )
        emailRegisterScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToPreferenceSetup = navController::navigateToPreferenceSetup,
        )
        passwordVerificationScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToPasswordReset = navController::navigateToPasswordReset,
            onNavigateToLogin = navController::navigateToLogin,
        )
        passwordResetScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToLogin = navController::navigateToLogin,
        )
        preferenceSetupScreen(
            metaData = metaData,
            onNavigateToHome = navController::navigateToHome,
        )

        homeScreen(
            bottomNav = bottomNav,
            onNavigateToSearch = navController::navigateToSearch,
            onNavigateToRanking = { navController.navigateToRanking() },
            onNavigateToExplore = { year, quarter ->
                navController.navigateToExplore(year = year, quarter = quarter)
            },
            onNavigateToHomeDetail = navController::navigateToHomeDetail,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
        )
        homeDetailScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
            onNavigateToReviewForm = navController::navigateToReviewForm,
        )

        infoAnimeScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToReviewForm = navController::navigateToReviewForm,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
            onNavigateToInfoSeries = navController::navigateToInfoSeries,
            onNavigateToInfoRecommend = navController::navigateToInfoRecommend,
            onNavigateToInfoCharacter = navController::navigateToInfoCharacter,
            onNavigateToActor = navController::navigateToActor,
            onNavigateToStudio = navController::navigateToStudio,
        )
        infoSeriesScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
        )
        infoRecommendScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
        )
        infoCharacterScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToActor = navController::navigateToActor
        )

        studioScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
        )

        actorScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
        )

        rankingScreen(
            metaData = metaData,
            bottomNav = bottomNav,
            onNavigateToSearch = navController::navigateToSearch,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
        )

        exploreScreen(
            metaData = metaData,
            bottomNav = bottomNav,
            onNavigateToSearch = navController::navigateToSearch,
            onNavigateToInfoAnime = navController::navigateToInfoAnime
        )

        myPageScreen(
            bottomNav = bottomNav,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
            onNavigateToUserContent = navController::navigateToUserContent,
            onNavigateToSetting = navController::navigateToSetting,
            onNavigateToActor = navController::navigateToActor,
        )
        userContentScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
            onNavigateToActor = navController::navigateToActor,
            onNavigateToReviewForm = navController::navigateToReviewForm,
        )

        settingScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToUserEdit = navController::navigateToUserEdit,
            onNavigateToLogin = navController::navigateToLogin,
        )
        userEditScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToLogin = navController::navigateToLogin,
        )

        searchScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
            onNavigateToSearchResult = navController::navigateToSearchResult
        )
        searchResultScreen(
            onNavigateBack = navController::navigateUp,
            onNavigateToInfoAnime = navController::navigateToInfoAnime,
            onNavigateToActor = navController::navigateToActor,
            onNavigateToStudio = navController::navigateToStudio,
        )

        /* review */
        reviewFormScreen(
            onNavigateBack = navController::navigateUp,
        )
    }
}