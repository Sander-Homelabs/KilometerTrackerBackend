import command.ActivateUserCommand

class ActivateUserUseCase(
    private val userConfirmationCodeRepo: UserConfirmationCodeRepository,
    private val userRepo: UserRepository,
    private val passwordRepo: UserPasswordRepository
) {
    suspend operator fun invoke(command: ActivateUserCommand) {

    }
}