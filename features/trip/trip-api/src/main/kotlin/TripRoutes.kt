import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import model.UserRole

fun Route.tripRoutes(registerTrip: RegisterTripUseCase) {
    route("/trip") {
        install(Authenticate)
        install(RequireRole) { listOf(UserRole.USER) }
    }

    post {
        registerTrip()

    }
}