package command

import java.util.UUID

data class ActivateUserCommand(
    val email: String,
    val password: String,
    val confirmationCode: UUID,
    val firstName: String,
    val lastName: String
)
