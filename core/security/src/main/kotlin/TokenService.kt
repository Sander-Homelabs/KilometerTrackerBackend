import model.UserRole

interface TokenService {
    fun verify(token: String): UserPrincipal
    suspend fun refresh(token: String): TokenPair
    suspend fun issue(email: String, role: UserRole): TokenPair
}