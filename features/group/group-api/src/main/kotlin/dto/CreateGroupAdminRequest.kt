package dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupAdminRequest(val groupName: String, val groupAdmin: String)