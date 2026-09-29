package dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(val accessToken: String, val refreshToken: String)
