import app.user.UserHandler
import io.ktor.server.application.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val userHandler: UserHandler by dependencies

    routing {
        userHandler.registerUserRoutes(this)
        userHandler.registerAdminPortalUserRoutes(this)
    }
}