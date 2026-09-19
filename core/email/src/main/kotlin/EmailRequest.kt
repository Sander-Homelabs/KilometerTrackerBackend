import java.util.UUID

sealed interface EmailRequest {
    val recipientEmail: String
    val workflow: EmailWorkflow

    data class ErrorNotification(
        override val recipientEmail: String,
        val error: String,
        val rawError: String? = null
    ) : EmailRequest {
        override val workflow = EmailWorkflow.ERROR_NOTIFICATION
    }

    data class InviteUser(
        override val recipientEmail: String,
        val admin: String,
        val inviteCode: UUID,
        val url: String
    ) : EmailRequest {
        override val workflow = EmailWorkflow.INVITE_USER
    }

    data class TankRefillNotification(
        override val recipientEmail: String,
        val costPerKilometer: Int,
        val start: Int,
        val end: Int,
        val tanker: String,
        val totalCost: Int,
        val costs: List<TankRefillCosts>
    ) : EmailRequest {
        override val workflow = EmailWorkflow.TANK_REFILL_NOTIFICATION
    }
}

data class TankRefillCosts(val cost: Int, val user: String)