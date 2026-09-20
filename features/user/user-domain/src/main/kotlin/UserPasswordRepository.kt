import model.Password

interface UserPasswordRepository {
    suspend fun findByEmail(email: String): List<Password>
    suspend fun findActiveByEmail(email: String): Password?
    suspend fun save(password: String, email: String)
    suspend fun disable(email: String)
}