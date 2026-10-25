package dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteTripRequest(val groupId: String, val start: Int)