package dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterTripRequest(
    val groupId: String,
    val start: Int,
    val end: Int,
    val email: String,
    val cost: Double?,
    val tanker: String?
)
