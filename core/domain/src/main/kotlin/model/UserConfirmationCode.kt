package model

import java.util.UUID
import kotlinx.datetime.Instant

data class UserConfirmationCode(val email: String, val code: UUID, val createdAt: Instant)