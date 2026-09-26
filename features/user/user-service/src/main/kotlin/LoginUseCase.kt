import exception.IncorrectPassword
import exception.InvalidEmail
import exception.UserNotFound
import extension.isValidEmail

class LoginUseCase(
    private val userRepo: UserRepository,
    private val passwordRepo: UserPasswordRepository,
    private val passwordHasher: PasswordHasher,
    private val tokenService: TokenService
) {
    suspend operator fun invoke(email: String, password: String): TokenPair {
        if (!email.isValidEmail()) throw InvalidEmail()
        password.validatePassword()

        val dbUser = userRepo.findActiveByEmail(email) ?: throw UserNotFound()
        val dbPassword = passwordRepo.findActiveByEmail(email) ?: throw UserNotFound()

        if (!passwordHasher.verify(password, dbPassword.password)) throw IncorrectPassword()

        return tokenService.issue(email, dbUser.role)
    }
}