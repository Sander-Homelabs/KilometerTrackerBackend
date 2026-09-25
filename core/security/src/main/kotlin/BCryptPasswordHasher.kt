import at.favre.lib.crypto.bcrypt.BCrypt

class BCryptPasswordHasher: PasswordHasher {
    override fun hash(password: String): String = BCrypt.withDefaults().hashToString(12, password.toCharArray())
    override fun verify(password: String, hash: String): Boolean = BCrypt.verifyer().verify(password.toCharArray(), hash).verified
}