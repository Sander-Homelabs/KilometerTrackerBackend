import dto.LoginRequest
import dto.LoginResponse
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(login: LoginUseCase) {
    route("/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val tokenPair = login(request.email, request.password)
            call.response.cookies.append(
                Cookie(
                    name = "Authorization",
                    value = tokenPair.accessToken,
                    maxAge = 60 * 15,
                    path = "/auth/login",
                    secure = System.getenv()["ENVIRONMENT"] == "PROD",
                    httpOnly = true,
                )
            )
            call.response.cookies.append(
                Cookie(
                    name = "Refresh",
                    value = tokenPair.refreshToken,
                    maxAge = 60 * 60 * 24 * 30,
                    path = "/auth/login",
                    secure = System.getenv()["ENVIRONMENT"] == "PROD",
                    httpOnly = true,
                )
            )
            call.respond(HttpStatusCode.OK, LoginResponse(tokenPair.accessToken, tokenPair.refreshToken))
        }
    }
}