class BCryptPasswordHasher: PasswordHasher {
    override fun hash(password: String): String = BCryptPasswordHasher().hash(password)
    override fun verify(password: String, hash: String): Boolean = BCryptPasswordHasher().verify(password, hash)
}