package app.user

import app.user.model.UserRegisterDto
import app.util.exception.DuplicateUser
import app.util.exception.InvalidEmail
import app.util.exception.MissingVariable
import app.util.extension.isValidEmail
import app.util.extension.validatePassword

class UserService(
    private val repository: UserRepository
) {
    fun registerUser(user: UserRegisterDto) {
        if (!user.email.isValidEmail()) throw InvalidEmail()
        user.password.validatePassword()
        if (user.firstName.isEmpty() || user.lastName.isEmpty()) throw MissingVariable()
        if (repository.getUser(user.email) != null) throw DuplicateUser()
        repository.addUser(user.email, user.password, user.firstName, user.lastName)
    }
}
