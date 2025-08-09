package com.jparkbro.model.home

import com.jparkbro.model.common.DefaultAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeRecommendResponse(
    @SerialName("referenceAnimeTitle")
    val referenceAnimeTitle: String? = null,
    @SerialName("animes")
    val animes: List<DefaultAnime> = emptyList()
)
