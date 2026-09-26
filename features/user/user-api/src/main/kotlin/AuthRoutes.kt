import dto.LoginRequest
import dto.LoginResponse
import dto.RefreshAccessTokenRequest
import exception.MissingRefreshToken
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.authRoutes(login: LoginUseCase, refresh: RefreshAccessTokenUseCase) {
    route("/auth") {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val tokenPair = login(request.email, request.password)
            call.response.cookies.append(
                Cookie(
                    name = "Authorization",
                    value = tokenPair.accessToken,
                    maxAge = 60 * 15,
                    path = "/",
                    secure = System.getenv()["ENVIRONMENT"] == "PROD",
                    httpOnly = true,
                )
            )
            call.response.cookies.append(
                Cookie(
                    name = "Refresh",
                    value = tokenPair.refreshToken,
                    maxAge = 60 * 60 * 24 * 30,
                    path = "/auth",
                    secure = System.getenv()["ENVIRONMENT"] == "PROD",
                    httpOnly = true,
                )
            )
            call.respond(HttpStatusCode.OK, LoginResponse(tokenPair.accessToken, tokenPair.refreshToken))
        }


        put("/refresh") {
            val cookieToken = call.request.cookies["Refresh"]
            val bodyToken = runCatching { call.receive<RefreshAccessTokenRequest>().refreshToken }.getOrNull()

            val refreshToken = bodyToken ?: cookieToken ?: throw MissingRefreshToken()

            val tokenPair = refresh(refreshToken)

            call.response.cookies.append(
                Cookie(
                    name = "Authorization",
                    value = tokenPair.accessToken,
                    maxAge = 60 * 15,
                    path = "/",
                    secure = System.getenv()["ENVIRONMENT"] == "PROD",
                    httpOnly = true,
                )
            )
            call.response.cookies.append(
                Cookie(
                    name = "Refresh",
                    value = tokenPair.refreshToken,
                    maxAge = 60 * 60 * 24 * 30,
                    path = "/auth",
                    secure = System.getenv()["ENVIRONMENT"] == "PROD",
                    httpOnly = true,
                )
            )
            call.respond(HttpStatusCode.OK, LoginResponse(tokenPair.accessToken, tokenPair.refreshToken))
        }
    }
}