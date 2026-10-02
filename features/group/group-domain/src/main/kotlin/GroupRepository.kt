import model.Group
import java.util.UUID

interface GroupRepository {
    suspend fun findById(id: UUID, email: String): Group?
    suspend fun findByEmail(email: String): List<Group>
    suspend fun save(name: String, email: String): Group?
    suspend fun update(id: UUID, name: String, email: String)
    suspend fun deactivate(id: UUID, email: String)
}