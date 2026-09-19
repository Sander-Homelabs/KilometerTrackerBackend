import exception.DuplicateUser
import exception.InvalidEmail
import extension.isValidEmail
import model.PendingUser
import model.User
import java.util.UUID

class RegisterUserUseCase(
    private val userConfirmationCodeRepo: UserConfirmationCodeRepository,
    private val userRepo: UserRepository,
    private val emailService: EmailService,
) {
    suspend operator fun invoke(email: String) {
        if (!email.isValidEmail()) throw InvalidEmail()

        when (userRepo.findByEmail(email)) {
            is User -> throw DuplicateUser()
            is PendingUser -> Unit
            null -> userRepo.save(PendingUser(email))
        }

        val code = UUID.randomUUID()
        userConfirmationCodeRepo.upsert(email, code)

        emailService.sendEmail(EmailRequest.RegisterUser(
            recipientEmail = email,
            activationCode = code,
            url = "https://kmtracker.goch.dev/activate/$code"
        ))
    }
}