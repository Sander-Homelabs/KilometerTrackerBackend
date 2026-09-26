import model.RefreshToken

interface RefreshTokenRepository {
    suspend fun findByEmail(email: String): List<RefreshToken>
    suspend fun findActiveByEmail(email: String): List<RefreshToken.Active>
    suspend fun save(email: String, token: String)
    suspend fun disable(email: String, token: String)
}