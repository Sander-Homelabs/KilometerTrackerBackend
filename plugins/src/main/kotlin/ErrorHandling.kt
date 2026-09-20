package plugins

import exception.DuplicateUser
import exception.InvalidEmail
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val error: String, val message: String)

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<InvalidEmail> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_EMAIL", cause.message ?: "Invalid email"))
        }
        exception<DuplicateUser> { call, cause ->
            call.respond(HttpStatusCode.Conflict, ErrorResponse("DUPLICATE_USER", cause.message ?: "User already exists"))
        }
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("INTERNAL_ERROR", "Something went wrong"))
        }
    }
}