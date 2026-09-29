package dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupAdminResponse(val id: String, val name: String, val createdBy: String)