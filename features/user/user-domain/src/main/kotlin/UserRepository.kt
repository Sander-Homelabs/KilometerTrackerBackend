import model.PendingUser
import model.User
import model.UserAccount

interface UserRepository {
    suspend fun findByEmail(email: String): UserAccount?
    suspend fun findActiveByEmail(email: String): User?
    suspend fun save(user: PendingUser)
    suspend fun update(email: String, user: User)
    suspend fun disable(email: String)
    suspend fun activate(email: String, user: User)
}