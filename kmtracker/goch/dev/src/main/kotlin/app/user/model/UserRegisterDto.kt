package app.user.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterDto(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)
