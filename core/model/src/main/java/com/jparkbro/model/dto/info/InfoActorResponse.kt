package com.jparkbro.model.dto.info

import com.jparkbro.model.common.actor.Cast
import com.jparkbro.model.common.actor.SimplePersonDto
import com.jparkbro.model.common.actor.toPerson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InfoActorResponse(
    @SerialName("character")
    val character: SimplePersonDto? = null,
    @SerialName("voiceActor")
    val voiceActor: SimplePersonDto? = null
)

fun InfoActorResponse.toCast() : Cast = Cast(
    character = character?.toPerson(),
    voiceActor = voiceActor?.toPerson(),
)