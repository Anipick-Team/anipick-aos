package com.jparkbro.model.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleAnime(
    @SerialName("animeId")
    override val animeId: Int,
    @SerialName("title")
    override val title: String,
    @SerialName("coverImageUrl")
    override val coverImageUrl: String
) : Anime