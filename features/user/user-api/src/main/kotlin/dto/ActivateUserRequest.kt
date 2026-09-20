package dto

import kotlinx.serialization.Serializable

@Serializable
data class ActivateUserRequest(
    val email: String,
    val password: String,
    val confirmationCode: String,
    val firstName: String,
    val lastName: String
)
