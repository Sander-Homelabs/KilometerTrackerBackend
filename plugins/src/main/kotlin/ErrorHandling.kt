package plugins

import exception.AccessTokenExpired
import exception.DatabaseError
import exception.DuplicateUser
import exception.IncorrectPassword
import exception.InvalidAccessToken
import exception.InvalidEmail
import exception.InvalidPassword
import exception.InvalidRefreshToken
import exception.InvalidUser
import exception.MissingAccessToken
import exception.MissingRefreshToken
import exception.NoActiveRefreshToken
import exception.RefreshTokenExpired
import exception.Forbidden
import exception.UserAlreadyActive
import exception.UserConfirmationCodeExpired
import exception.UserConfirmationCodeNotFound
import exception.UserNotFound
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.ContentTransformationException
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
        exception<IncorrectPassword> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("INCORRECT_PASSWORD", cause.message ?: "Incorrect password"))
        }
        exception<AccessTokenExpired> { call, cause ->
            call.respond(HttpStatusCode.Gone, ErrorResponse("ACCESS_TOKEN_EXPIRED", cause.message ?: "Access token expired"))
        }
        exception<InvalidAccessToken> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_ACCESS_TOKEN", cause.message ?: "Invalid access token"))
        }
        exception<RefreshTokenExpired> { call, cause ->
            call.respond(HttpStatusCode.Gone, ErrorResponse("REFRESH_TOKEN_EXPIRED", cause.message ?: "Refresh token expired"))
        }
        exception<InvalidRefreshToken> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_REFRESH_TOKEN", cause.message ?: "Invalid refresh token"))
        }
        exception<NoActiveRefreshToken> { call, cause ->
            call.respond(HttpStatusCode.Gone, ErrorResponse("NO_ACTIVE_REFRESH_TOKEN", cause.message ?: "No active refresh token"))
        }
        exception<MissingRefreshToken> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ErrorResponse("MISSING_REFRESH_TOKEN", cause.message ?: "Missing refresh token"))
        }
        exception<MissingAccessToken> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("MISSING_ACCESS_TOKEN", cause.message ?: "Missing access token"))
        }
        exception<Forbidden> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("FORBIDDEN", cause.message ?: "Forbidden"))
        }
        exception<ContentTransformationException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("MALFORMED_REQUEST_BODY", "Failed to convert request body"))
        }
        exception<BadRequestException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_REQUEST_BODY", "Bad request"))
        }
        exception<DatabaseError> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("DATABASE_ERROR", cause.message ?: "Database error"))
        }
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("INTERNAL_ERROR", "Something went wrong"))
        }
    }
}