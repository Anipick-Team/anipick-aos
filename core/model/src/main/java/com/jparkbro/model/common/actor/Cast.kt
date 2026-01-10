package com.jparkbro.model.common.actor

import com.jparkbro.model.enum.CastRole

data class Cast(
    val character: Person? = null,
    val voiceActor: Person? = null,
    val role: CastRole? = null,
)
