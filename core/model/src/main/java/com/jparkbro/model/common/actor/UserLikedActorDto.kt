package com.jparkbro.model.common.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLikedActorDto(
    @SerialName("personId")
    val personId: Long,
    @SerialName("userLikedVoiceActorId")
    val userLikedVoiceActorId: Int,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
)

fun UserLikedActorDto.toPerson() : Person = Person(
    id = personId,
    userLikedVoiceActorId = userLikedVoiceActorId,
    name = name,
    imageUrl = profileImageUrl
)
