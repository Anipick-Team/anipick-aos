package com.jparkbro.model.common.actor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPersonDto(
    @SerialName("personId")
    val id: Long? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profileImage")
    val imageUrl: String? = null,
)

fun SearchPersonDto.toPerson() : Person = Person(
    id = id,
    name = name,
    imageUrl = imageUrl,
)