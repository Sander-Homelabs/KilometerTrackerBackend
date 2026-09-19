import model.UserConfirmationCode
import java.util.UUID

interface UserConfirmationCodeRepository {
    suspend fun findByEmail(email: String): UserConfirmationCode?
    suspend fun upsert(email: String, code: UUID)
    suspend fun delete(email: String, code: UUID)
}