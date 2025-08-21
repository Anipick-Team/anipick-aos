package com.jparkbro.review.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jparkbro.model.common.FormType
import com.jparkbro.review.ReviewForm
import com.jparkbro.review.ReviewFormViewModel
import kotlinx.serialization.Serializable

@Serializable data class ReviewForm(val animeId: Int, val reviewId: Int?, val type: FormType)

fun NavController.navigateToReviewForm(
    animeId: Int,
    reviewId: Int? = null,
    type: FormType,
    navOptions: NavOptions? = null
) = navigate(ReviewForm(animeId, reviewId, type), navOptions)

fun NavGraphBuilder.reviewFormScreen(
    onNavigateBack: () -> Unit,
    onPopBackWithRefresh: () -> Unit,
) {
    composable<ReviewForm> { entry ->
        val route = entry.toRoute<ReviewForm>()

        val info = ReviewForm(animeId = route.animeId, reviewId = route.reviewId, type = route.type)

        ReviewForm(
            onNavigateBack = onNavigateBack,
            onPopBackWithRefresh = onPopBackWithRefresh,
            viewModel = hiltViewModel<ReviewFormViewModel, ReviewFormViewModel.Factory>(
                key = "${route.type}"
            ) { factory ->
                factory.create(info)
            }
        )
    }
}