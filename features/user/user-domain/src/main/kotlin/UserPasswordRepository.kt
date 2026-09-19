interface UserPasswordRepository {
    suspend fun findByEmail(email: String): String?
    suspend fun save(password: String, email: String)
    suspend fun disable(email: String)
}