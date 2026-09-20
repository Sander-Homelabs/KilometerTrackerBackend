package dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(val email: String)
