import exception.MissingAccessToken
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.util.AttributeKey
import org.koin.java.KoinJavaComponent.inject

val userPrincipalKey = AttributeKey<UserPrincipal>("UserPrincipal")

val Authenticate = createRouteScopedPlugin("Authenticate") {
    onCall { call ->
        val token = call.request.headers["Authorization"] ?: call.request.cookies["Authorization"] ?: throw MissingAccessToken()

        val tokenService: TokenService by inject(TokenService::class.java)

        val tokenPair = tokenService.verify(token)
        call.attributes.put(userPrincipalKey, tokenPair)
    }
}

fun ApplicationCall.getUserPrincipal(): UserPrincipal? = attributes.getOrNull(userPrincipalKey)