package com.jparkbro.review

interface ReviewFormAction {
    data object OnRetryClicked : ReviewFormAction
    data object NavigateBack : ReviewFormAction
    data class OnRatingChanged(val rating: Float) : ReviewFormAction
    data object OnSpoilerClicked : ReviewFormAction
    data object NavigateToGuideline : ReviewFormAction
    data object OnSaveReviewClicked : ReviewFormAction
}