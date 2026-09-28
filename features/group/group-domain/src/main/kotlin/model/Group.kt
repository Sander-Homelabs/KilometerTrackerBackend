package model

import java.util.UUID

data class Group(val id: UUID, val name: String, val createdBy: String, val users: List<GroupUser> = emptyList())
