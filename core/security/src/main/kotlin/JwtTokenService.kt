import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import exception.AccessTokenExpired
import exception.InvalidAccessToken
import exception.InvalidRefreshToken
import exception.NoActiveRefreshToken
import exception.RefreshTokenExpired
import model.UserRole
import java.time.Instant
import java.time.temporal.ChronoUnit

class JwtTokenService(private val refreshTokenRepo: RefreshTokenRepository): TokenService {
    private val accessTokenSecret = System.getenv("ACCESS_TOKEN_SECRET")
        ?: error("ACCESS_TOKEN_SECRET is not set")

    private val refreshTokenSecret = System.getenv("REFRESH_TOKEN_SECRET")
        ?: error("REFRESH_TOKEN_SECRET is not set")

    private val accessAlgorithm = Algorithm.HMAC256(accessTokenSecret)
    private val accessExpiresAt = ChronoUnit.MINUTES.duration.multipliedBy(15)
    private val refreshAlgorithm = Algorithm.HMAC256(refreshTokenSecret)
    private val refreshExpiresAt = ChronoUnit.DAYS.duration.multipliedBy(30)

    override fun verify(token: String): UserPrincipal {
        return verifyAccessToken(token)
    }

    override suspend fun refresh(token: String): TokenPair {
        val decoded = verifyRefreshToken(token)

        val dbToken = refreshTokenRepo.findActiveByEmail(decoded.email).find { it.token == token }
        if (dbToken == null) throw NoActiveRefreshToken()

        val newTokens = issue(decoded.email, decoded.role)
        refreshTokenRepo.disable(decoded.email, token)

        return newTokens
    }

    override suspend fun issue(email: String, role: UserRole): TokenPair {
        val accessToken = generateAccessToken(email, role)
        val refreshToken = generateRefreshToken(email, role)

        refreshTokenRepo.save(email, refreshToken)

        return TokenPair(accessToken, refreshToken)
    }

    private fun generateAccessToken(email: String, role: UserRole): String {
        return JWT.create()
            .withClaim("email", email)
            .withClaim("role", role.dbValue)
            .withExpiresAt(Instant.now().plus(accessExpiresAt))
            .sign(accessAlgorithm)
    }

    private fun generateRefreshToken(email: String, role: UserRole): String {
        return JWT.create()
            .withClaim("email", email)
            .withClaim("role", role.dbValue)
            .withExpiresAt(Instant.now().plus(refreshExpiresAt))
            .sign(refreshAlgorithm)
    }

    private fun verifyAccessToken(token: String): UserPrincipal {
        try {
            val decoded = JWT.require(accessAlgorithm).build().verify(token)
            val email = decoded.getClaim("email").asString()
            val role = decoded.getClaim("role").asString().let { UserRole.fromDbValue(it) }
            return UserPrincipal(email, role)
        } catch (_: TokenExpiredException) {
            throw AccessTokenExpired()
        } catch (_: JWTVerificationException) {
            throw InvalidAccessToken()
        }
    }

    private fun verifyRefreshToken(token: String): UserPrincipal {
        try {
            val decoded = JWT.require(refreshAlgorithm).build().verify(token)
            val email = decoded.getClaim("email").asString()
            val role = decoded.getClaim("role").asString().let { UserRole.fromDbValue(it) }
            return UserPrincipal(email, role)
        } catch (_: TokenExpiredException) {
            throw RefreshTokenExpired()
        } catch (_: JWTVerificationException) {
            throw InvalidRefreshToken()
        }
    }
}