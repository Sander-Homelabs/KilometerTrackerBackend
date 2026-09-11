package app.util.exception

import io.ktor.http.HttpStatusCode

class DuplicateUser: AppException(
    statusCode = HttpStatusCode.Conflict,
    message = "duplicate_user"
)
