package app.util.exception

import io.ktor.http.HttpStatusCode

class MissingVariable: AppException(
    statusCode = HttpStatusCode.BadRequest,
    message = "missing_variables"
)