package app.user

import app.user.model.UserRegisterDto
import app.util.exception.AppException
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

class UserHandler(
    private val service: UserService
) {
    fun registerUserRoutes(route: Route) {
        route.route("/user") {
            post {
                try {
                    val userDto = call.receive<UserRegisterDto>()
                    service.registerUser(userDto)
                    call.respond(HttpStatusCode.Created)
                } catch(e: AppException) {
                    call.respond(e.statusCode, e.message)
                }
            }
        }
    }

    fun registerAdminPortalUserRoutes(route: Route) {
        route.route("/admin/user") {
            post {

            }
        }
    }
}