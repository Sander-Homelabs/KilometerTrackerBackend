import command.RegisterTripCommand
import dto.RegisterTripRequest
import exception.Unauthenticated
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import model.UserRole
import java.util.UUID

fun Route.tripRoutes(registerTrip: RegisterTripUseCase) {
    route("/trip") {
        install(Authenticate)
        install(RequireRole) { listOf(UserRole.USER) }

        post {
            val request = call.receive<RegisterTripRequest>()
            registerTrip(
                RegisterTripCommand(
                    groupId = UUID.fromString(request.groupId),
                    start = request.start,
                    end = request.end,
                    email = request.email,
                    registeredBy = call.getUserPrincipal()?.email ?: throw Unauthenticated()
                )
            )
            call.respond(HttpStatusCode.Created)
        }
    }
}