package com.jparkbro.model.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailActor(
    @SerialName("character")
    val character: AnimePerson,
    @SerialName("voiceActor")
    val voiceActor: AnimePerson
)

@Serializable
data class AnimePerson(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String? = "",
    @SerialName("imageUrl")
    val imageUrl: String,
)