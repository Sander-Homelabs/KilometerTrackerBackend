package command

data class ActivateUserCommand(
    val email: String,
    val password: String,
    val confirmationCode: String,
    val firstName: String,
    val lastName: String
)
