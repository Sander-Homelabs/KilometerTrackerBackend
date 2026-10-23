package command

import java.util.UUID

data class RegisterTripCommand(
    val groupId: UUID,
    val start: Int,
    val end: Int,
    val email: String,
    val registeredBy: String
)
