package com.jparkbro.model.dto.actor

import com.jparkbro.model.common.Cursor
import com.jparkbro.model.common.actor.ActorFilmographyDto
import com.jparkbro.model.common.actor.toDomain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetActorResponse(
    @SerialName("personId")
    val personId: Long? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
    @SerialName("isLiked")
    val isLiked: Boolean? = null,
    @SerialName("count")
    val count: Int? = null,
    @SerialName("cursor")
    val cursor: Cursor? = null,
    @SerialName("works")
    val works: List<ActorFilmographyDto> = emptyList(),
)

fun GetActorResponse.toResult() : GetActorResult = GetActorResult(
    personId = personId,
    name = name,
    profileImageUrl = profileImageUrl,
    isLiked = isLiked,
    count = count,
    cursor = cursor,
    works = works.map { it.toDomain() }
)