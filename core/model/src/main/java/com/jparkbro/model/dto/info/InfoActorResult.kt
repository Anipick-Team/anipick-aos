package com.jparkbro.model.dto.info

import com.jparkbro.model.common.actor.Person

data class InfoActorResult(
    val character: Person,
    val voiceActor: Person
)
