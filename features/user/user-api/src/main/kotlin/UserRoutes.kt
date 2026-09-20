import dto.RegisterUserRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.userRoutes(register: RegisterUserUseCase) {
    route("/user") {
        post {
            val request = call.receive<RegisterUserRequest>()
            register(request.email)
            call.respond(HttpStatusCode.Created)
        }
    }
}