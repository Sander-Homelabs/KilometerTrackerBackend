package app.util.exception

import io.ktor.http.HttpStatusCode

class InvalidPassword(val reasons: List<String>): AppException(
    statusCode = HttpStatusCode.BadRequest,
    message = "invalid_password"
)