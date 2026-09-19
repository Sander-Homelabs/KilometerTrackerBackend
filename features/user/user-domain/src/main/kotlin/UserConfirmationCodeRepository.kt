import java.util.UUID

interface UserConfirmationCodeRepository {
    suspend fun findByEmail(email: String): UUID?
    suspend fun save(email: String, code: UUID)
    suspend fun delete(email: String, code: UUID)
}