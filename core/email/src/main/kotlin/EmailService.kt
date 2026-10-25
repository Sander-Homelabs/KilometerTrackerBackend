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
            is EmailRequest.UserAdded -> mapOf(
                "admin" to request.admin,
                "group" to request.group,
            )
            is EmailRequest.TankRefillNotification -> mapOf(
                "costPerKilometer" to request.costPerKilometer,
                "costs" to request.costs.map { cost ->
                    mapOf(
                        "cost" to cost.cost,
                        "user" to cost.user
                    )
                },
                "end" to request.end,
                "start" to request.start,
                "tanker" to request.tanker,
                "totalCost" to request.totalCost,
            )
            is EmailRequest.RegisterUser -> mapOf(
                "activation-code" to request.activationCode,
                "url" to request.url,
            )
        }

        val workflowData = data.entries.fold(WorkflowTriggerParams.Data.builder()) { builder, (key, value) ->
            builder.putAdditionalProperty(key, JsonValue.from(value))
        }.build()

        val workFlowTriggerParams = WorkflowTriggerParams.builder()
            .key(request.workflow.knockKey)
            .data(workflowData)

        request.recipientEmail.forEach { workFlowTriggerParams.addRecipient(it) }

        client.workflows().trigger(workFlowTriggerParams.build())
    }
}