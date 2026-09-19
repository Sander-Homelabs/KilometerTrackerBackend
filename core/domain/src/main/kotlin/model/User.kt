package model

sealed interface UserAccount {
    val email: String
    val status: UserStatus
    val role: UserRole
}

data class PendingUser(
    override val email: String,
    override val status: UserStatus = UserStatus.AWAITING_CONFIRMATION,
    override val role: UserRole = UserRole.USER
) : UserAccount

data class User(
    override val email: String,
    val firstName: String,
    val lastName: String,
    override val status: UserStatus = UserStatus.ACTIVE,
    override val role: UserRole = UserRole.USER
) : UserAccount