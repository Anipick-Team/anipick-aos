package com.jparkbro.model.common.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorFilmographyDto(
    @SerialName("animeId")
    val animeId: Long? = null,
    @SerialName("animeTitle")
    val animeTitle: String? = null,
    @SerialName("characterId")
    val characterId: Long? = null,
    @SerialName("characterName")
    val characterName: String? = null,
    @SerialName("characterImageUrl")
    val characterImageUrl: String? = null,
)

fun ActorFilmographyDto.toDomain() : Filmography = Filmography(
    animeId = animeId,
    animeTitle = animeTitle,
    characterId = characterId,
    characterName = characterName,
    characterImageUrl = characterImageUrl
)