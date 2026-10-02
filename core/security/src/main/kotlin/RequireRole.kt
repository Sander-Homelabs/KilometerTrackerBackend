import exception.MissingAccessToken
import exception.Forbidden
import io.ktor.server.application.createRouteScopedPlugin
import model.UserRole
import org.koin.java.KoinJavaComponent.inject

class RequireRoleParams {
    var role: List<UserRole> = listOf(UserRole.USER)
}

val RequireRole = createRouteScopedPlugin("RequireRole", ::RequireRoleParams) {
    onCall { call ->
        val token = call.request.headers["Authorization"] ?: call.request.cookies["Authorization"] ?: throw MissingAccessToken()

        val tokenService: TokenService by inject(TokenService::class.java)

        val userPrincipal = tokenService.verify(token)

        if (pluginConfig.role.none { it == userPrincipal.role }) throw Forbidden()
    }
}