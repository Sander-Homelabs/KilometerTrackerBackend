package app.util.exception

import io.ktor.http.HttpStatusCode

class InvalidEmail: AppException(
    statusCode = HttpStatusCode.BadRequest,
    message = "invalid_email"
)
