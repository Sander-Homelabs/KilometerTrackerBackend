import command.ActivateUserCommand
import dto.ActivateUserRequest
import dto.RegisterUserRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.userRoutes(register: RegisterUserUseCase, activate: ActivateUserUseCase) {
    route("/user") {
        post {
            val request = call.receive<RegisterUserRequest>()
            register(request.email)
            call.respond(HttpStatusCode.Created)
        }

        post("/activate") {
            val request = call.receive<ActivateUserRequest>()
            activate(
                ActivateUserCommand(
                    email = request.email,
                    password = request.password,
                    confirmationCode = request.confirmationCode,
                    firstName = request.firstName,
                    lastName = request.lastName
                )
            )
            call.respond(HttpStatusCode.OK)
        }
    }
}