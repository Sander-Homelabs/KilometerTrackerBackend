import dto.CreateGroupAdminRequest
import dto.CreateGroupAdminResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import model.UserRole

fun Route.groupRoutes(adminCreate: AdminCreateGroupUseCase) {
    route("/group") {
        install(Authenticate)

        post {

        }

        route("/admin") {
            install(RequireRole) { listOf(UserRole.ADMIN) }

            post {
                val request = call.receive<CreateGroupAdminRequest>()
                val group = adminCreate(request.groupName, request.groupAdmin)
                call.respond(HttpStatusCode.Created, CreateGroupAdminResponse(
                    id = group.id.toString(),
                    name = group.name,
                    createdBy = group.createdBy
                ))
            }
        }
    }
}