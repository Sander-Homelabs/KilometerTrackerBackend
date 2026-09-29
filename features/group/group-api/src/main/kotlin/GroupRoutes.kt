import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import model.UserRole

fun Route.groupRoutes() {
    route("/group") {
        install(Authenticate)

        post {

        }

        route("/admin") {
            install(RequireRole) { listOf(UserRole.ADMIN) }
        }
    }
}