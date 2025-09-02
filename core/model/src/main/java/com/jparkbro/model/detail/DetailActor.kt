package com.jparkbro.model.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailActor(
    @SerialName("character")
    val character: AnimePerson,
    @SerialName("voiceActor")
    val voiceActor: AnimePerson,
    @SerialName("role")
    val role: CharacterRole? = null,
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

@Serializable
enum class CharacterRole {
    @SerialName("MAIN")
    MAIN,
    @SerialName("SUPPORTING")
    SUPPORTING,
    @SerialName("BACKGROUND")
    BACKGROUND
}