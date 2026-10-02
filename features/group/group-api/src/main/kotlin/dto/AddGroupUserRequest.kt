package dto

import kotlinx.serialization.Serializable

@Serializable
data class AddGroupUserRequest(val groupId: String, val user: String)