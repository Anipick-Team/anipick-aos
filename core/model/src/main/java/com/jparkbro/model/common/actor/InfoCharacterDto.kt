package com.jparkbro.model.common.actor

import com.jparkbro.model.enum.CastRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoCharacterDto(
    @SerialName("character")
    val character: SimplePersonDto? = null,
    @SerialName("voiceActor")
    val voiceActor: SimplePersonDto? = null,
    @SerialName("role")
    val role: CastRole? = null,
)

fun InfoCharacterDto.toCast() : Cast = Cast(
    character = character?.toPerson(),
    voiceActor = voiceActor?.toPerson(),
    role = role
)