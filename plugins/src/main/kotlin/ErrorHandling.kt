package plugins

import exception.DuplicateUser
import exception.InvalidEmail
import exception.InvalidPassword
import exception.InvalidUser
import exception.UserAlreadyActive
import exception.UserConfirmationCodeExpired
import exception.UserConfirmationCodeNotFound
import exception.UserNotFound
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
        exception<InvalidPassword> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_PASSWORD", cause.message ?: "Invalid password"))
        }
        exception<UserNotFound> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ErrorResponse("USER_NOT_FOUND", cause.message ?: "User not found"))
        }
        exception<UserAlreadyActive> { call, cause ->
            call.respond(HttpStatusCode.Conflict, ErrorResponse("USER_ALREADY_ACTIVE", cause.message ?: "User already active"))
        }
        exception<UserConfirmationCodeNotFound> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ErrorResponse("USER_CONFIRMATION_CODE_NOT_FOUND", cause.message ?: "User confirmation code not found"))
        }
        exception<UserConfirmationCodeExpired> { call, cause ->
            call.respond(HttpStatusCode.Gone, ErrorResponse("USER_CONFIRMATION_CODE_EXPIRED", cause.message ?: "User confirmation code expired"))
        }
        exception<InvalidUser> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_USER", cause.message ?: "Invalid user"))
        }
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("INTERNAL_ERROR", "Something went wrong"))
        }
    }
}