import app.knock.api.client.okhttp.KnockOkHttpClient
import app.knock.api.core.JsonValue
import app.knock.api.models.workflows.WorkflowTriggerParams
import io.github.cdimascio.dotenv.Dotenv

class EmailService {
    private val client = KnockOkHttpClient.builder().apiKey(Dotenv.load()["KNOCK_API_KEY"]).build()

    fun sendEmail(request: EmailRequest) {
        val data: Map<String, Any?> = when (request) {
            is EmailRequest.ErrorNotification -> mapOf(
                "error" to request.error,
                "raw-error" to request.rawError
            )
            is EmailRequest.InviteUser -> mapOf(
                "admin" to request.admin,
                "inviteCode" to request.inviteCode.toString(),
                "url" to request.url
            )
            is EmailRequest.TankRefillNotification -> mapOf(
                "costPerKilometer" to request.costPerKilometer,
                "costs" to request.costs,
                "end" to request.end,
                "start" to request.start,
                "tanker" to request.tanker,
                "totalCost" to request.totalCost,
            )
        }

        val workflowData = data.entries.fold(WorkflowTriggerParams.Data.builder()) { builder, (key, value) ->
            builder.putAdditionalProperty(key, JsonValue.from(value))
        }.build()

        client.workflows().trigger(
            WorkflowTriggerParams.builder()
                .key(request.workflow.knockKey)
                .addRecipient(request.recipientEmail)
                .data(workflowData)
                .build()
        )
    }
}