package com.jparkbro.model.detail

import com.jparkbro.model.common.Cursor
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnimeActorsResponse(
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("characters")
    val characters: List<DetailActor>
)
