package com.jparkbro.model.dto.info

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.InfoCharacterDto
import com.jparkbro.model.common.actor.toCast
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetInfoCharactersResponse(
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("characters")
    val casts: List<InfoCharacterDto> = emptyList()
)

fun GetInfoCharactersResponse.toResult() : GetInfoCharactersResult = GetInfoCharactersResult(
    cursor = cursor,
    casts = casts.map { it.toCast() }
)