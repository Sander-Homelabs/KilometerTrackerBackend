class RefreshAccessTokenUseCase(
    private val tokenService: TokenService
) {
    suspend operator fun invoke(token: String): TokenPair {
        return tokenService.refresh(token)
    }
}