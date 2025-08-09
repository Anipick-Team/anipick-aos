package com.jparkbro.model.home

import com.jparkbro.model.common.DefaultAnime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpcomingSeasonItems(
    @SerialName("season")
    val season: Int,
    @SerialName("seasonYear")
    val seasonYear: Int,
    @SerialName("animes")
    val animes: List<DefaultAnime>,
)