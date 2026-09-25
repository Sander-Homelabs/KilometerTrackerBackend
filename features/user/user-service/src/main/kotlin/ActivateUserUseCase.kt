import command.ActivateUserCommand
import exception.InvalidUser
import exception.UserAlreadyActive
import exception.UserConfirmationCodeExpired
import exception.UserConfirmationCodeNotFound
import exception.UserNotFound
import kotlinx.datetime.Clock
import model.User
import model.UserStatus
import kotlin.time.Duration.Companion.hours

class ActivateUserUseCase(
    private val userConfirmationCodeRepo: UserConfirmationCodeRepository,
    private val userRepo: UserRepository,
    private val passwordRepo: UserPasswordRepository,
    private val passwordHasher: PasswordHasher
) {
    suspend operator fun invoke(command: ActivateUserCommand) {
        val user = userRepo.findByEmail(command.email) ?: throw UserNotFound()
        if (user.status != UserStatus.AWAITING_CONFIRMATION) throw UserAlreadyActive()

        val userConfirmationCode = userConfirmationCodeRepo.findByEmail(user.email) ?: throw UserConfirmationCodeNotFound()

        if (userConfirmationCode.code != command.confirmationCode) throw UserConfirmationCodeNotFound()
        if (Clock.System.now() > userConfirmationCode.createdAt + 24.hours) throw UserConfirmationCodeExpired()

        if (command.firstName.isEmpty() || command.lastName.isEmpty()) throw InvalidUser()

        command.password.validatePassword()
        val hash = passwordHasher.hash(command.password)

        passwordRepo.save(hash, user.email)
        userRepo.activate(user.email, User(command.email, command.firstName, command.lastName))
        userConfirmationCodeRepo.delete(user.email, userConfirmationCode.code)
    }
}