package com.jparkbro.model.common.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimplePersonDto(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
)

fun SimplePersonDto.toPerson() : Person = Person(
    id = id,
    name = name,
    imageUrl = imageUrl,
)