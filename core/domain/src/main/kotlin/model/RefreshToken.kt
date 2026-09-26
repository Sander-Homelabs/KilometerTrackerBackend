package model

sealed interface RefreshToken {
    val email: String
    val token: String
    val active: Boolean

    data class Active(
        override val email: String,
        override val token: String,
        override val active: Boolean = true
    ) : RefreshToken

    data class Inactive(
        override val email: String,
        override val token: String,
        override val active: Boolean = false
    ) : RefreshToken
}