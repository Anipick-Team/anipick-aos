package com.jparkbro.review.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jparkbro.model.common.FormType
import com.jparkbro.review.ReviewFormRoot
import kotlinx.serialization.Serializable

@Serializable data class ReviewForm(val animeId: Long, val formType: FormType)

fun NavController.navigateToReviewForm(
    animeId: Long,
    formType: FormType,
    navOptions: NavOptions? = null
) = navigate(ReviewForm(animeId, formType), navOptions)

fun NavGraphBuilder.reviewFormScreen(
    onNavigateBack: () -> Unit
) {
    composable<ReviewForm> {
        ReviewFormRoot(
            onNavigateBack = onNavigateBack
        )
    }
}