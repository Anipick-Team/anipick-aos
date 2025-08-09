package com.jparkbro.model.search

import com.jparkbro.model.common.DefaultAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("popularAnimes")
    val popularAnimes: List<DefaultAnime>
)
