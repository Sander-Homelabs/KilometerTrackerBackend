import model.GroupRole
import model.GroupUser
import java.util.UUID

interface GroupUserRepository {
    suspend fun save(email: String, groupId: UUID, userEmail: String, role: GroupRole = GroupRole.MEMBER): GroupUser?
}