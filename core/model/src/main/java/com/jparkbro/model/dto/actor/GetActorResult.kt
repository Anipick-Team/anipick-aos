package com.jparkbro.model.dto.actor

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.Filmography

data class GetActorResult(
    val personId: Long? = null,
    val name: String? = null,
    val profileImageUrl: String? = null,
    val isLiked: Boolean? = null,
    val count: Int? = null,
    val cursor: Cursor? = null,
    val works: List<Filmography> = emptyList(),
)
