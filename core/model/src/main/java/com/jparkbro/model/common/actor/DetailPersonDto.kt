package com.jparkbro.model.common.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailPersonDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String?,
    @SerialName("imageUrl")
    val imageUrl: String?,
)

fun DetailPersonDto.toPerson() : Person = Person(
    id = id,
    name = name,
    imageUrl = imageUrl,
)