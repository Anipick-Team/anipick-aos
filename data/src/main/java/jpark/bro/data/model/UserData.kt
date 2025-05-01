package jpark.bro.data.model

import jpark.bro.domain.model.User

data class UserData(
    val id: String,
    val displayName: String,
    val profilePictureUrl: String,
    val idToken: String? = null
) {
    fun toDomainModel(): User {
        return User(
            id = id,
            displayName = displayName,
            profilePictureUrl = profilePictureUrl
        )
    }
}
