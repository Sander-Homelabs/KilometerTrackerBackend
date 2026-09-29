package dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupAdminRequest(val groupId: String, val groupName: String, val createdBy: String)