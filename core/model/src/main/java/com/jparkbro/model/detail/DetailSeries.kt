package com.jparkbro.model.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailSeries(
    @SerialName("animeId")
    val animeId: Int,
    @SerialName("title")
    val title: String?,
    @SerialName("coverImageUrl")
    val coverImageUrl: String,
    @SerialName("airDate")
    val airDate: String,
)
