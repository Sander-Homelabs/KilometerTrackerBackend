import java.util.UUID

sealed interface EmailRequest {
    val recipientEmail: List<String>
    val workflow: EmailWorkflow

    data class ErrorNotification(
        override val recipientEmail: List<String>,
        val error: String,
        val rawError: String? = null
    ) : EmailRequest {
        override val workflow = EmailWorkflow.ERROR_NOTIFICATION
    }

    data class InviteUser(
        override val recipientEmail: List<String>,
        val admin: String,
        val inviteCode: UUID,
        val url: String
    ) : EmailRequest {
        override val workflow = EmailWorkflow.INVITE_USER
    }

    data class UserAdded(
        override val recipientEmail: List<String>,
        val group: String,
        val admin: String,
    ) : EmailRequest {
        override val workflow = EmailWorkflow.USER_ADDED
    }

    data class TankRefillNotification(
        override val recipientEmail: List<String>,
        val costPerKilometer: Double,
        val start: Int,
        val end: Int,
        val tanker: String,
        val totalCost: Double,
        val costs: List<TankRefillCosts>
    ) : EmailRequest {
        override val workflow = EmailWorkflow.TANK_REFILL_NOTIFICATION
    }

    data class RegisterUser(
        override val recipientEmail: List<String>,
        val activationCode: UUID,
        val url: String
    ) : EmailRequest {
        override val workflow = EmailWorkflow.REGISTER_USER
    }
}

data class TankRefillCosts(val cost: Double, val user: String)