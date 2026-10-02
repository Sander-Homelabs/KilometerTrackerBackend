import exception.Forbidden
import model.GroupRole
import model.PendingUser
import model.UserAccount
import java.util.UUID

class AddGroupUserUseCase(
    private val userRepo: UserRepository,
    private val userConfirmationCodeRepo: UserConfirmationCodeRepository,
    private val groupUserRepo: GroupUserRepository,
    private val emailService: EmailService
) {
    suspend operator fun invoke(admin: String, groupId: UUID, userEmail: String) {
        val groupUsers = groupUserRepo.findByGroupId(groupId, admin)
        if (groupUsers.none { it.email == admin && it.role == GroupRole.ADMIN }) throw Forbidden()
        val user = userRepo.findByEmail(userEmail)
        when (user) {
            is UserAccount -> Unit
            else -> {
                userRepo.save(PendingUser(userEmail))

                val code = UUID.randomUUID()
                userConfirmationCodeRepo.upsert(userEmail, code)

                emailService.sendEmail(EmailRequest.InviteUser(
                    recipientEmail = userEmail,
                    admin = admin,
                    inviteCode = code,
                    url = "https://kmtracker.goch.dev/user/activate"
                ))
            }
        }
        groupUserRepo.save(admin, groupId, userEmail)
    }
}