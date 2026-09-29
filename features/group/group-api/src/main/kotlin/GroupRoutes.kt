import dto.CreateGroupAdminRequest
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
                call.receive<CreateGroupAdminRequest>()
                val group = adminCreate()
                call.respond(HttpStatusCode.Created, CreateGroupAdminRequest(
                    groupId = group.id.toString(),
                    groupName = group.name,
                    createdBy = group.createdBy
                ))
            }
        }
    }
}