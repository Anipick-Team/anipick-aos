package com.jparkbro.review

import androidx.compose.foundation.text.input.TextFieldState
import com.jparkbro.model.common.FormType
import com.jparkbro.model.common.UiState
import com.jparkbro.model.common.review.Review

data class ReviewFormState(
    val uiState: UiState = UiState.Loading,
    val formType: FormType = FormType.CREATE,
    val content: TextFieldState = TextFieldState(),

    val isAnimeRatingLoading: Boolean = false,

    /* API 통신 로딩 */
    val isLoading: Boolean = false,

    /* API 통신 데이터 */
    val animeReview: Review? = null
) {
    val isSaveEnabled: Boolean
        get() = (animeReview?.rating ?: 0f) > 0f && content.text.isNotBlank()
}
