package command

import java.util.UUID

data class RegisterTankRefillCommand(
    val groupId: UUID,
    val start: Int,
    val end: Int,
    val cost: Double,
    val tanker: String,

    val registeredBy: String
)
