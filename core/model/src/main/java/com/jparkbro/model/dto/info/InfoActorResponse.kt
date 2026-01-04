package com.jparkbro.model.dto.info

import com.jparkbro.model.common.actor.DetailPersonDto
import com.jparkbro.model.common.actor.toPerson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoActorResponse(
    @SerialName("character")
    val character: DetailPersonDto,
    @SerialName("voiceActor")
    val voiceActor: DetailPersonDto
)

fun InfoActorResponse.toResult() : InfoActorResult = InfoActorResult(
    character = character.toPerson(),
    voiceActor = voiceActor.toPerson()
)