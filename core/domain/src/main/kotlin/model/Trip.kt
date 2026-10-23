package model

import java.util.UUID

data class Trip(
    val groupId: UUID,
    val start: Int,
    val end: Int,
    val email: String,
)
