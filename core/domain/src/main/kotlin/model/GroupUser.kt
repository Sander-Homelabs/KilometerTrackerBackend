package model

import java.util.UUID

data class GroupUser(val groupId: UUID, val email: String, val role: GroupRole)
