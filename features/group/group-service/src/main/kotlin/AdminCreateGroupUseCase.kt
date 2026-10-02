import exception.DatabaseError
import model.Group
import model.GroupRole

class AdminCreateGroupUseCase(
    private val groupRepo: GroupRepository,
    private val groupUserRepo: GroupUserRepository
) {
    suspend operator fun invoke(groupName: String, groupAdmin: String): Group {
        val group = groupRepo.save(groupName, groupAdmin) ?: throw DatabaseError()
        val user = groupUserRepo.save(email = groupAdmin, groupId = group.id, userEmail = groupAdmin, role = GroupRole.ADMIN)
        if (user == null) {
            groupRepo.deactivate(group.id, groupAdmin)
            throw DatabaseError()
        }
        return group
    }
}