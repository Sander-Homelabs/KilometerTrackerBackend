package dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshAccessTokenRequest(val refreshToken: String)