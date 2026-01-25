package com.jparkbro.model.dto.search

import com.jparkbro.model.common.anime.SimpleAnimeDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPopularAnimeResponse(
    @SerialName("popularAnimes")
    val popularAnimes: List<SimpleAnimeDto>
)